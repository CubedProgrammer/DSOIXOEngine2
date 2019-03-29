package tictactoe.main;
import java.util.*;
import tictactoe.board.*;
public class CommandLineInputHandler
{
	private volatile boolean requested;
	private volatile byte depth;
	private volatile EngineBrain engine;
	private volatile Board board;
	private boolean calculating;
	private boolean running;
	private Thread moveCalculationThread;
	private Thread handlerThread;
	public CommandLineInputHandler()
	{
		this.handlerThread=new Thread(this::handle);
		this.moveCalculationThread=new Thread(this::search);
		this.board=new Board();
		this.engine=new EngineBrain(this.board,false,(byte)2);
	}
	public void parse(String...args)
	{
		if(args[0].equals("exit"))
		{
			this.running=false;
		}
		else if(args[0].equals("position"))
		{
			if(args.length==2)
			{
				if(args[1].length()==9)
				{
					Board board=TicTacToeCanvas.getBoard(args[1].toUpperCase().replace('S',' '));
					byte x=board.count((byte)1);
					byte o=board.count((byte)2);
					if(x==o||x==o+1)
					{
						this.board=board;
						this.engine.setTurn(this.board.isOTurn());
						this.engine.setPosition(this.board);
					}
					else
					{
						System.out.println("Invalid position!");
					}
				}
				else
				{
					System.out.println("Invalid argument!");
				}
			}
			else
			{
				System.out.println("Command position only accepts one argument!");
			}
		}
		else if(args[0].equals("analyse"))
		{
			if(args.length==2)
			{
				try
				{
					this.depth=Byte.parseByte(args[1]);
					this.requested=true;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				this.depth=(byte)9;
				this.requested=true;
			}
		}
		else
		{
			System.out.println("Unknown command!");
		}
	}
	public void handle()
	{
		String command="";
		String[]args=new String[0];
		Scanner scanner=new Scanner(System.in);
		while(this.running)
		{
			if(scanner.hasNextLine())
			{
				command=scanner.nextLine();
				args=command.split(" ");
				if(args.length>0)
				{
					this.parse(args);
				}
			}
		}
		scanner.close();
		System.exit(0);
	}
	public void search()
	{
		while(this.calculating)
		{
			if(this.requested)
			{
				this.engine.analyse(this.depth);
				this.requested=false;
			}
		}
		System.exit(0);
	}
	public synchronized void start()
	{
		this.running=true;
		this.calculating=true;
		this.handlerThread.start();
		this.moveCalculationThread.start();
	}
}