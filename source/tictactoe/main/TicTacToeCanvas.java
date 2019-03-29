package tictactoe.main;
import java.awt.*;
import java.awt.image.*;
import tictactoe.board.*;
import tictactoe.evt.*;
public class TicTacToeCanvas extends Canvas
{
	public static final long serialVersionUID=-2552800943146L;
	private Thread tickRenderThread;
	private Thread moveCalculationThread;
	private volatile boolean playerSymbolO;
	private volatile boolean analysing;
	private boolean running;
	private boolean calculating;
	private volatile boolean engineMadeMove;
	private volatile EngineBrain engine;
	private volatile Board board;
	private volatile byte finished;
	private boolean showEngineStatistics;
	private Color resetColor;
	private Color changeColor;
	private Color difficultyColor;
	private Color modeColor;
	public static final Board getBoard(String board)
	{
		byte[]pos=new byte[9];
		for(int i=0;i<pos.length;i++)
		{
			if(board.charAt(i)==88)
			{
				pos[i]=1;
			}
			else if(board.charAt(i)==79)
			{
				pos[i]=2;
			}
		}
		return new Board(pos);
	}
	public TicTacToeCanvas()
	{
		MouseEvtHandler m=new MouseEvtHandler();
		this.setSize(970,600);
		this.setPreferredSize(this.getSize());
		this.addMouseListener(m);
		this.addMouseMotionListener(m);
		this.board=new Board();
		this.engine=new EngineBrain(this.board,true,(byte)0);
		this.resetColor=new Color(18,18,18);
		this.changeColor=new Color(18,18,18);
		this.difficultyColor=new Color(18,18,18);
		this.modeColor=new Color(18,18,18);
		new TicTacToeWindow(this);
	}
	public void paint(Graphics2D g)
	{
		g.setColor(new Color(18,18,18));
		g.fillRect(215,205,540,10);
		g.fillRect(215,385,540,10);
		g.fillRect(390,30,10,540);
		g.fillRect(570,30,10,540);
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(this.board.symbolAtLocation(j,i)==88)
				{
					g.setColor(new Color(18,18,238));
					for(int k=0;k<150;k++)
					{
						g.fillRect(j*180+230+(k<<2)/5,i*180+45+k,30,1);
						g.fillRect(j*180+350-(k<<2)/5,i*180+45+k,30,1);
					}
				}
				else if(this.board.symbolAtLocation(j,i)==79)
				{
					g.setColor(new Color(238,18,18));
					g.fillOval(j*180+230,i*180+45,150,150);
					g.setColor(new Color(238,236,238));
					g.fillOval(j*180+245,i*180+60,120,120);
				}
			}
		}
		g.setColor(this.resetColor);
		g.fillRect(245,575,200,20);
		g.setColor(this.changeColor);
		g.fillRect(525,575,200,20);
		g.setColor(this.difficultyColor);
		g.fillRect(245,5,200,20);
		g.setColor(this.modeColor);
		g.fillRect(525,5,200,20);
		g.setFont(new Font("Tahoma",0,15));
		FontMetrics fm=g.getFontMetrics();
		String difficulty="Difficulty: "+(this.engine.getDifficulty()==0?"Easy":this.engine.getDifficulty()==1?"Hard":"Undefeatable");
		String mode=this.analysing?"Mode: Analysis":"Mode: Play Against CPU";
		g.setColor(new Color(255-this.resetColor.getRed(),255-this.resetColor.getGreen(),255-this.resetColor.getBlue()));
		g.drawString("Restart Game",345-(fm.stringWidth("Restart Game")>>>1),585+(fm.getAscent()+fm.getDescent()>>>1)-fm.getDescent());
		g.setColor(new Color(255-this.changeColor.getRed(),255-this.changeColor.getGreen(),255-this.changeColor.getBlue()));
		g.drawString("Change Symbol",625-(fm.stringWidth("Change Symbol")>>>1),585+(fm.getAscent()+fm.getDescent()>>>1)-fm.getDescent());
		g.setColor(new Color(255-this.difficultyColor.getRed(),255-this.difficultyColor.getGreen(),255-this.difficultyColor.getBlue()));
		g.drawString(difficulty,345-(fm.stringWidth(difficulty)>>>1),15+(fm.getAscent()+fm.getDescent()>>>1)-fm.getDescent());
		g.setColor(new Color(255-this.modeColor.getRed(),255-this.modeColor.getGreen(),255-this.modeColor.getBlue()));
		g.drawString(mode,625-(fm.stringWidth(mode)>>>1),15+(fm.getAscent()+fm.getDescent()>>>1)-fm.getDescent());
		if(this.finished>0)
		{
			g.setFont(new Font("Tahoma",0,60));
			g.setColor(new Color(18,238,18));
			fm=g.getFontMetrics();
			String msg=this.finished==1?"X Wins":this.finished==2?"O Wins":"Draw";
			g.drawString(msg,485-(fm.stringWidth(msg)>>>1),300+(fm.getAscent()+fm.getDescent()>>>1)-fm.getDescent());
		}
		if(this.showEngineStatistics)
		{
			g.setFont(new Font("Tahoma",0,16));
			g.setColor(new Color(18,18,18));
			fm=g.getFontMetrics();
			String evaluation=this.engine.getEvaluationAsString();
			int depth=this.engine.getSearchDepth();
			int nodes=this.engine.getPositionsSearched();
			g.drawString("Best Move: "+evaluation.substring(0,2),755,290);
			g.drawString("Evaluation: "+evaluation.substring(3),755,320);
			g.drawString("Depth Searched: "+Integer.toString(depth),215-fm.stringWidth("Depth Searched: "+Integer.toString(depth)),290);
			g.drawString("Positions Searched: "+Integer.toString(nodes),215-fm.stringWidth("Positions Searched: "+Integer.toString(nodes)),320);
		}
	}
	public synchronized void start()
	{
		this.running=true;
		this.calculating=true;
		this.tickRenderThread=new Thread(this::run);
		this.moveCalculationThread=new Thread(this::calculate);
		this.tickRenderThread.start();
		this.moveCalculationThread.start();
	}
	public synchronized void stop()
	{
		System.exit(0);
	}
	public void tick()
	{
		MouseEvtHandler evtm=(MouseEvtHandler)this.getMouseListeners()[0];
		if(evtm.isOnRestart())
		{
			this.resetColor=new Color(36,36,36);
		}
		else if(evtm.isOnChange())
		{
			this.changeColor=new Color(36,36,36);
		}
		else if(evtm.isOnDifficulty())
		{
			this.difficultyColor=new Color(36,36,36);
		}
		else if(evtm.isOnMode())
		{
			this.modeColor=new Color(36,36,36);
		}
		else
		{
			this.resetColor=new Color(18,18,18);
			this.changeColor=new Color(18,18,18);
			this.difficultyColor=new Color(18,18,18);
			this.modeColor=new Color(18,18,18);
		}
		if(evtm.didRestart())
		{
			this.board=new Board();
			this.engine.setPosition(this.board);
			if(this.analysing)
			{
				this.engine.setTurn(this.board.isOTurn());
			}
			evtm.setRestarted(false);
		}
		else if(evtm.didChange())
		{
			this.board=new Board();
			this.engine.setPosition(this.board);
			if(this.analysing)
			{
				this.engine.setTurn(this.board.isOTurn());
			}
			else
			{
				this.engine.setTurn(this.playerSymbolO);
				this.playerSymbolO=!playerSymbolO;
			}
			this.engine.setPosition(this.board);
			evtm.setChanged(false);
		}
		else if(evtm.didDifficultyChange())
		{
			if(!this.analysing)
			{
				this.engine.setDifficulty((byte)(this.engine.getDifficulty()<2?this.engine.getDifficulty()+1:0));
			}
			evtm.setDifficultyChanged(false);
		}
		else if(evtm.didModeChange())
		{
			this.analysing=!this.analysing;
			this.engine.setDifficulty((byte)2);
			if(this.analysing)
			{
				this.engine.setTurn(this.board.isOTurn());
				this.engineMadeMove=false;
				this.showEngineStatistics=true;
			}
			else
			{
				this.engine.setTurn(!this.playerSymbolO);
				this.showEngineStatistics=false;
			}
			evtm.setModeChanged(false);
		}
		else if(evtm.getShowStatistics())
		{
			this.showEngineStatistics=!this.showEngineStatistics;
			evtm.setShowStatistics(false);
		}
		if(this.analysing)
		{
			if(evtm.didPlayMove())
			{
				if(this.finished==0&&this.board.symbolAtLocation(evtm.getOnGrid()%3,evtm.getOnGrid()/3)==32)
				{
					this.board.playMove(evtm.getOnGrid()%3,evtm.getOnGrid()/3);
					this.engineMadeMove=false;
					this.engine.setTurn(!this.engine.isO());
				}
				evtm.setPlayMove(false);
			}
		}
		else
		{
			this.finished=this.board.isFinished();
			if(this.finished==0)
			{
				if(this.playerSymbolO?this.board.isOTurn():!this.board.isOTurn())
				{
					if(evtm.didPlayMove())
					{
						if(this.finished==0&&this.board.symbolAtLocation(evtm.getOnGrid()%3,evtm.getOnGrid()/3)==32)
						{
							this.board.playMove(evtm.getOnGrid()%3,evtm.getOnGrid()/3);
						}
						evtm.setPlayMove(false);
						this.engineMadeMove=false;
					}
				}
				else
				{
					if(this.engine.isFinishedCalculating()&&this.finished==0)
					{
						this.board.playMove((this.engine.getEvaluation()>>>8)%3,(this.engine.getEvaluation()>>>8)/3);
					}
				}
			}
		}
	}
	public void render()
	{
		BufferStrategy bs=this.getBufferStrategy();
		if(bs==null)
		{
			this.createBufferStrategy(3);
			return;
		}
		Graphics g=bs.getDrawGraphics();
		g.setColor(new Color(238,238,238));
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		this.paint((Graphics2D)g);
		g.dispose();
		bs.show();
	}
	public void run()
	{
		long then=System.nanoTime();
		long now=System.nanoTime();
		long timeElapsed=0;
		int fps=60;
		while(this.running)
		{
			now=System.nanoTime();
			timeElapsed+=now-then;
			then=now;
			if(timeElapsed>=1000000000/fps)
			{
				this.tick();
				this.render();
				timeElapsed=0;
			}
		}
		this.stop();
	}
	public void calculate()
	{
		while(this.calculating)
		{
			if(this.analysing)
			{
				if(!this.engineMadeMove)
				{
					this.finished=this.board.isFinished();
					if(this.finished==0)
					{
						this.engine.setPosition(this.board);
						this.engine.analyse((byte)9);
						this.engineMadeMove=true;
					}
				}
			}
			else
			{
				if(this.board.isOTurn()==this.engine.isO())
				{
					if(!this.engineMadeMove)
					{
						this.finished=this.board.isFinished();
						if(this.finished==0)
						{
							this.engine.setPosition(this.board);
							this.engine.analyse((byte)9);
							this.engineMadeMove=true;
						}
					}
				}
			}
		}
	}
	public static final void main(String...args)
	{
		new CommandLineInputHandler().start();
		if(args.length>0)
		{
			if(args[args.length-1].equals("gui"))
			{
				new TicTacToeCanvas().start();
			}
		}
	}
}