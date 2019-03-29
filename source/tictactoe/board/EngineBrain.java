package tictactoe.board;
import java.util.*;
public class EngineBrain
{
	private volatile Board board;
	private volatile short evaluation;
	private volatile boolean finished;
	private volatile boolean isO;
	private volatile byte difficulty;
	private volatile int positions;
	private volatile int depth;
	public EngineBrain(Board board,boolean o,byte difficulty)
	{
		this.board=board;
		this.finished=true;
		this.isO=o;
		this.difficulty=difficulty;
	}
	public Board getPosition()
	{
		return this.board;
	}
	public short getEvaluation()
	{
		return this.evaluation;
	}
	public int getPositionsSearched()
	{
		return this.positions;
	}
	public int getSearchDepth()
	{
		return this.depth;
	}
	public String getEvaluationAsString()
	{
		return(this.isO?"O":"X")+Integer.toString(this.evaluation>>>8)+" "+Byte.toString((byte)(this.evaluation&0xFF));
	}
	public boolean isFinishedCalculating()
	{
		return this.finished;
	}
	public boolean isO()
	{
		return this.isO;
	}
	public byte getDifficulty()
	{
		return this.difficulty;
	}
	public void setPosition(Board board)
	{
		this.board=board;
	}
	public void setTurn(boolean isO)
	{
		this.isO=isO;
	}
	public void setDifficulty(byte difficulty)
	{
		this.difficulty=difficulty;
	}
	private byte onlyMove()
	{
		byte b=-1;
		byte filled=0;
		for(int i=0;i<9;i++)
		{
			if(this.board.symbolAtLocation(i%3,i/3)!=32)
			{
				filled++;
			}
			else
			{
				b=(byte)i;
			}
		}
		if(filled<8)
		{
			b=-1;
		}
		return b;
	}
	private byte canWin()
	{
		byte b=-1;
		char ally=(char)(this.isO?79:88);
		char[]line=new char[3];
		byte allies=0;
		byte space=-1;
		for(int i=0;i<3;i++)
		{
			line[0]=this.board.symbolAtLocation(0,i);
			line[1]=this.board.symbolAtLocation(1,i);
			line[2]=this.board.symbolAtLocation(2,i);
			for(int j=0;j<line.length;j++)
			{
				if(line[j]==ally)
				{
					allies++;
				}
				if(line[j]==32)
				{
					space=(byte)(i*3+j);
				}
			}
			if(allies==2&&space>=0)
			{
				b=space;
				i=3;
			}
			space=-1;
			allies=0;
		}
		if(b==-1)
		{
			for(int i=0;i<3;i++)
			{
				line[0]=this.board.symbolAtLocation(i,0);
				line[1]=this.board.symbolAtLocation(i,1);
				line[2]=this.board.symbolAtLocation(i,2);
				for(int j=0;j<line.length;j++)
				{
					if(line[j]==ally)
					{
						allies++;
					}
					if(line[j]==32)
					{
						space=(byte)(j*3+i);
					}
				}
				if(allies==2&&space>=0)
				{
					b=space;
					i=3;
				}
				space=-1;
				allies=0;
			}
		}
		if(b==-1)
		{
			line[0]=this.board.symbolAtLocation(0,0);
			line[1]=this.board.symbolAtLocation(1,1);
			line[2]=this.board.symbolAtLocation(2,2);
			for(int i=0;i<line.length;i++)
			{
				if(line[i]==ally)
				{
					allies++;
				}
				if(line[i]==32)
				{
					space=(byte)(i*4);
				}
			}
			if(allies==2&&space>=0)
			{
				b=space;
			}
			space=-1;
			allies=0;
		}
		if(b==-1)
		{
			line[0]=this.board.symbolAtLocation(2,0);
			line[1]=this.board.symbolAtLocation(1,1);
			line[2]=this.board.symbolAtLocation(0,2);
			for(int i=0;i<line.length;i++)
			{
				if(line[i]==ally)
				{
					allies++;
				}
				if(line[i]==32)
				{
					space=(byte)(2+i*2);
				}
			}
			if(allies==2&&space>=0)
			{
				b=space;
			}
			space=-1;
			allies=0;
		}
		return b;
	}
	private byte detectThreat()
	{
		byte b=-1;
		char enemy=(char)(this.isO?88:79);
		char[]line=new char[3];
		byte enemies=0;
		byte space=-1;
		for(int i=0;i<3;i++)
		{
			line[0]=this.board.symbolAtLocation(0,i);
			line[1]=this.board.symbolAtLocation(1,i);
			line[2]=this.board.symbolAtLocation(2,i);
			for(int j=0;j<line.length;j++)
			{
				if(line[j]==enemy)
				{
					enemies++;
				}
				if(line[j]==32)
				{
					space=(byte)(i*3+j);
				}
			}
			if(enemies==2&&space>=0)
			{
				b=space;
				i=3;
			}
			space=-1;
			enemies=0;
		}
		if(b==-1)
		{
			for(int i=0;i<3;i++)
			{
				line[0]=this.board.symbolAtLocation(i,0);
				line[1]=this.board.symbolAtLocation(i,1);
				line[2]=this.board.symbolAtLocation(i,2);
				for(int j=0;j<line.length;j++)
				{
					if(line[j]==enemy)
					{
						enemies++;
					}
					if(line[j]==32)
					{
						space=(byte)(j*3+i);
					}
				}
				if(enemies==2&&space>=0)
				{
					b=space;
					i=3;
				}
				space=-1;
				enemies=0;
			}
		}
		if(b==-1)
		{
			line[0]=this.board.symbolAtLocation(0,0);
			line[1]=this.board.symbolAtLocation(1,1);
			line[2]=this.board.symbolAtLocation(2,2);
			for(int i=0;i<line.length;i++)
			{
				if(line[i]==enemy)
				{
					enemies++;
				}
				if(line[i]==32)
				{
					space=(byte)(i*4);
				}
			}
			if(enemies==2&&space>=0)
			{
				b=space;
			}
			space=-1;
			enemies=0;
		}
		if(b==-1)
		{
			line[0]=this.board.symbolAtLocation(2,0);
			line[1]=this.board.symbolAtLocation(1,1);
			line[2]=this.board.symbolAtLocation(0,2);
			for(int i=0;i<line.length;i++)
			{
				if(line[i]==enemy)
				{
					enemies++;
				}
				if(line[i]==32)
				{
					space=(byte)(2+i*2);
				}
			}
			if(enemies==2&&space>=0)
			{
				b=space;
			}
			space=-1;
			enemies=0;
		}
		return b;
	}
	private byte detectDoubleThreat()
	{
		byte b=-1;
		char[]row=new char[3];
		char[]column=new char[3];
		char enemy=(char)(this.isO?88:79);
		byte intersection=-1;
		byte rowspaces=0;
		byte columnspaces=0;
		boolean rowenemy=false;
		boolean columnenemy=false;
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<2;j++)
			{
				row[0]=this.board.symbolAtLocation(0,i*2);
				row[1]=this.board.symbolAtLocation(1,i*2);
				row[2]=this.board.symbolAtLocation(2,i*2);
				column[0]=this.board.symbolAtLocation(j*2,0);
				column[1]=this.board.symbolAtLocation(j*2,1);
				column[2]=this.board.symbolAtLocation(j*2,2);
				for(int cell=0;cell<row.length&&cell<column.length;cell++)
				{
					if(row[cell]==32)
					{
						rowspaces++;
					}
					if(column[cell]==32)
					{
						columnspaces++;
					}
					rowenemy=rowenemy||row[cell]==enemy;
					columnenemy=columnenemy||column[cell]==enemy;
				}
				if(rowenemy&&columnenemy&&rowspaces==2&&columnspaces==2)
				{
					if(this.board.symbolAtLocation(j*2,i*2)==enemy)
					{
						if(this.board.symbolAtLocation(2-j*2,2-i*2)==(this.isO?79:88))
						{
							intersection=-1;
						}
						else
						{
							intersection=(byte)(6-i*6+2-j*2);
						}
					}
					else
					{
						intersection=(byte)(i*6+j*2);
					}
				}
				if(intersection>=0)
				{
					b=intersection;
					j=2;
					i=2;
				}
				rowenemy=false;
				columnenemy=false;
				rowspaces=0;
				columnspaces=0;
			}
		}
		return b;
	}
	private void evaluate(byte depth)
	{
		Random r=new Random(0x97de3173214acbl^System.currentTimeMillis());
		int move=-1;
		switch(this.difficulty)
		{
			case 0:
				if(this.board.symbolAtLocation(1,1)==32)
				{
					this.evaluation=0x0400;
				}
				else
				{
					move=this.canWin();
					if(move>=0)
					{
						this.evaluation=(short)(move<<8);
					}
					else
					{
						move=Math.abs(r.nextInt()%9);
						while(this.board.symbolAtLocation(move%3,move/3)!=32)
						{
							move=Math.abs(r.nextInt()%9);
						}
						this.evaluation=(short)(move<<8);
					}
				}
				break;
			case 1:
				if(this.board.isEmpty())
				{
					move=Math.abs(r.nextInt()%9);
					this.evaluation=(short)(move<<8);
				}
				else if((move=this.onlyMove())>=0)
				{
					this.evaluation=(short)(move<<8);
				}
				else if((move=this.canWin())>=0)
				{
					this.evaluation=(short)(move<<8);
				}
				else if((move=this.detectThreat())>=0)
				{
					this.evaluation=(short)(move<<8);
				}
				else if((move=this.detectDoubleThreat())>=0)
				{
					this.evaluation=(short)(move<<8);
				}
				else if(this.board.symbolAtLocation(1,1)==32)
				{
					this.evaluation=0x0400;
				}
				else
				{
					char ul=this.board.symbolAtLocation(0,0);
					char ur=this.board.symbolAtLocation(2,0);
					char bl=this.board.symbolAtLocation(2,0);
					char br=this.board.symbolAtLocation(2,2);
					if(ul==32)
					{
						this.evaluation=0;
					}
					else if(ur==32)
					{
						this.evaluation=0x0200;
					}
					else if(bl==32)
					{
						this.evaluation=0x0600;
					}
					else if(br==32)
					{
						this.evaluation=0x0800;
					}
					else
					{
						move=r.nextInt();
						while(this.board.symbolAtLocation(move%3,move%9/3)!=32)
						{
							move=r.nextInt();
						}
						this.evaluation=(short)(Math.abs(move)%9<<8);
					}
				}
				break;
			default:
				ArrayList<ArrayList<Board>>variationLayers=new ArrayList<ArrayList<Board>>();
				boolean oturn=this.isO;
				byte evaluation=0;
				while(depth>=0)
				{
					variationLayers.add(new ArrayList<Board>());
					if(variationLayers.size()==1)
					{
						for(int i=0;i<9;i++)
						{
							if(this.board.symbolAtLocation(i%3,i/3)==32)
							{
								variationLayers.get(0).add(this.board.clone());
								variationLayers.get(0).get(variationLayers.get(0).size()-1).playMove(i%3,i/3);
							}
						}
					}
					else
					{
						for(int i=0;i<variationLayers.get(variationLayers.size()-2).size();i++)
						{
							if((evaluation=variationLayers.get(variationLayers.size()-2).get(i).isFinished())==0&&depth>0)
							{
								for(byte j=0;j<9;j++)
								{
									if(variationLayers.get(variationLayers.size()-2).get(i).symbolAtLocation(j%3,j/3)==32)
									{
										variationLayers.get(variationLayers.size()-1).add(variationLayers.get(variationLayers.size()-2).get(i).clone());
										variationLayers.get(variationLayers.size()-1).get(variationLayers.get(variationLayers.size()-1).size()-1).playMove(j%3,j/3);
										variationLayers.get(variationLayers.size()-1).get(variationLayers.get(variationLayers.size()-1).size()-1).setParent(i);
									}
								}
							}
							else
							{
								variationLayers.get(variationLayers.size()-2).get(i).setEvaluation((byte)(evaluation==2?-1:evaluation%3));
							}
						}
						if(variationLayers.get(variationLayers.size()-1).size()==0)
						{
							variationLayers.remove(variationLayers.size()-1);
							depth=0;
						}
					}
					oturn=!oturn;
					depth--;
				}
				int p=0;
				int positions=0;
				for(int i=variationLayers.size()-1;i>=0;i--)
				{
					positions+=variationLayers.get(i).size();
					for(int j=0;j<variationLayers.get(i).size();j++)
					{
						if(variationLayers.get(i).get(j).getParent()!=p||j==0)
						{
							evaluation=variationLayers.get(i).get(j).getEvaluation();
							p=variationLayers.get(i).get(j).getParent();
						}
						else
						{
							if(oturn)
							{
								evaluation=(byte)(Math.min(evaluation,variationLayers.get(i).get(j).getEvaluation()));
							}
							else
							{
								evaluation=(byte)(Math.max(evaluation,variationLayers.get(i).get(j).getEvaluation()));
							}
						}
						if(i>0)
						{
							variationLayers.get(i-1).get(variationLayers.get(i).get(j).getParent()).setEvaluation(evaluation);
						}
						else
						{
							this.board.setEvaluation(evaluation);
						}
					}
					oturn=!oturn;
				}
				int variation=0;
				int negate=this.isO?-1:1;
				for(int i=0;i<variationLayers.get(0).size();i++)
				{
					variation=variationLayers.get(0).get(i).getEvaluation()*negate>variationLayers.get(0).get(variation).getEvaluation()*negate?i:variation;
				}
				move=variationLayers.get(0).get(variation).getPreviousMove();
				this.depth=variationLayers.size();
				this.positions=positions;
				this.evaluation=(short)(move<<8|(evaluation&0xFF));
				System.out.println("Depth: "+Integer.toString(this.depth)+", Positions: "+Integer.toString(this.positions));
				System.out.println("Evaluation: "+this.getEvaluationAsString());
				break;
		}
		this.finished=true;
	}
	public void analyse(byte depth)
	{
		this.finished=false;
		long start=System.currentTimeMillis();
		this.evaluate(depth);
		System.out.println("Time took to analyse to depth of "+Integer.toString(this.depth)+" is "+Long.toString(System.currentTimeMillis()-start)+" milliseconds.");
	}
}