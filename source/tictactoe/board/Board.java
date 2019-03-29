package tictactoe.board;
public class Board
{
	private byte[]position;
	private byte evaluation;
	private byte previousmove;
	private int parent;
	private boolean oTurn;
	public static final int range(int...set)
	{
		int maximum=-2147432768;
		int minimum=2147432767;
		for(int s:set)
		{
			maximum=s>maximum?s:maximum;
			minimum=s<minimum?s:minimum;
		}
		return maximum-minimum;
	}
	public Board()
	{
		this.position=new byte[9];
	}
	public Board(byte...position)
	{
		byte z=0;
		for(int i=0;i<position.length;i++)
		{
			if(position[i]==0)
			{
				z++;
			}
		}
		if((z&1)==0)
		{
			this.oTurn=true;
		}
		this.position=position;
	}
	public final boolean isOTurn()
	{
		return this.oTurn;
	}
	public final byte getEvaluation()
	{
		return this.evaluation;
	}
	public final byte getPreviousMove()
	{
		return this.previousmove;
	}
	public final int getParent()
	{
		return this.parent;
	}
	public final boolean isEmpty()
	{
		boolean empty=true;
		for(int i=0;i<this.position.length;i++)
		{
			empty=empty&&this.position[i]==32;
		}
		return empty;
	}
	public final void setEvaluation(byte evaluation)
	{
		this.evaluation=evaluation;
	}
	public final void setPreviousMove(byte previousmove)
	{
		this.previousmove=previousmove;
	}
	public final void setParent(int parent)
	{
		this.parent=parent;
	}
	public final byte count(byte symbol)
	{
		byte total=0;
		for(int i=0;i<this.position.length;i++)
		{
			if(this.position[i]==symbol)
			{
				total++;
			}
		}
		return total;
	}
	public final byte isFinished()
	{
		byte finished=0;
		int[]line=new int[3];
		boolean zeros=false;
		for(int i=0;i<3;i++)
		{
			zeros=zeros||this.position[i*3]==0;
			zeros=zeros||this.position[i*3+1]==0;
			zeros=zeros||this.position[i*3+2]==0;
		}
		for(int i=0;i<3;i++)
		{
			line[0]=this.position[i*3];
			line[1]=this.position[i*3+1];
			line[2]=this.position[i*3+2];
			if(Board.range(line)==0&&line[0]>0)
			{
				finished=(byte)line[0];
				i=3;
			}
			if(i<3)
			{
				line[0]=this.position[i];
				line[1]=this.position[3+i];
				line[2]=this.position[6+i];
				if(Board.range(line)==0&&line[0]>0)
				{
					finished=(byte)line[0];
					i=3;
				}
			}
		}
		if(Board.range(this.position[0],this.position[4],this.position[8])==0&&this.position[4]>0)
		{
			finished=this.position[4];
		}
		if(Board.range(this.position[2],this.position[4],this.position[6])==0&&this.position[4]>0)
		{
			finished=this.position[4];
		}
		finished=zeros||finished>0?finished:3;
		return finished;
	}
	public final char symbolAtLocation(int x,int y)
	{
		byte b=y*3+x>=0?this.position[y*3+x]:3;
		if(b==0)
		{
			return 32;
		}
		else if(b==1)
		{
			return 88;
		}
		else if(b==2)
		{
			return 79;
		}
		return 13;
	}
	public final void playMove(int x,int y)
	{
		if(y*3+x<9&&y*3+x>=0&&this.isFinished()==0)
		{
			byte b=this.position[y*3+x];
			this.position[y*3+x]=b==0?(byte)(this.oTurn?2:1):this.position[y*3+x];
			this.previousmove=(byte)(y*3+x);
			this.oTurn=b>0?this.oTurn:!this.oTurn;
		}
	}
	public final String toString()
	{
		String s="";
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(this.position[i*3+j]==0)
				{
					s+=" ";
				}
				else if(this.position[i*3+j]==1)
				{
					s+="X";
				}
				else if(this.position[i*3+j]==2)
				{
					s+="O";
				}
			}
			s+="\r";
		}
		return s;
	}
	public final Board clone()
	{
		Board board=new Board();
		for(int i=0;i<board.position.length;i++)
		{
			board.position[i]=this.position[i];
		}
		board.oTurn=this.oTurn;
		board.evaluation=this.evaluation;
		board.previousmove=this.previousmove;
		board.parent=this.parent;
		return board;
	}
}