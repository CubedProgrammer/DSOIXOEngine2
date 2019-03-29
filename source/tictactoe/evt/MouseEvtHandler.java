package tictactoe.evt;
import java.awt.event.*;
public class MouseEvtHandler extends MouseAdapter
{
	private byte onGrid;
	private boolean playedMove;
	private boolean onRestart;
	private boolean onChange;
	private boolean onDifficulty;
	private boolean onMode;
	private boolean restarted;
	private boolean changed;
	private boolean difficulty;
	private boolean mode;
	private boolean statistics;
	public MouseEvtHandler()
	{
		this.onGrid=-1;
	}
	public void mousePressed(MouseEvent evt)
	{
		if(this.onGrid>=0)
		{
			this.playedMove=true;
		}
	}
	public void mouseMoved(MouseEvent evt)
	{
		this.onGrid=-1;
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(evt.getX()>j*180+215&&evt.getX()<j*180+395&&evt.getY()>i*180+30&&evt.getY()<i*180+210)
				{
					this.onGrid=(byte)(i*3+j);
				}
			}
		}
		if(evt.getX()>245&&evt.getX()<445&&evt.getY()>575&&evt.getY()<595)
		{
			this.onRestart=true;
		}
		else if(evt.getX()>525&&evt.getX()<725&&evt.getY()>575&&evt.getY()<595)
		{
			this.onChange=true;
		}
		else if(evt.getX()>245&&evt.getX()<445&&evt.getY()>5&&evt.getY()<25)
		{
			this.onDifficulty=true;
		}
		else if(evt.getX()>525&&evt.getX()<725&&evt.getY()>5&&evt.getY()<25)
		{
			this.onMode=true;
		}
		else
		{
			this.onRestart=false;
			this.onChange=false;
			this.onDifficulty=false;
			this.onMode=false;
		}
	}
	public void mouseClicked(MouseEvent evt)
	{
		if(this.onRestart)
		{
			this.restarted=true;
		}
		else if(this.onChange)
		{
			this.changed=true;
		}
		else if(this.onDifficulty)
		{
			this.difficulty=true;
		}
		else if(this.onMode)
		{
			this.mode=true;
		}
		else if(this.onGrid==-1)
		{
			this.statistics=true;
		}
	}
	public byte getOnGrid()
	{
		return this.onGrid;
	}
	public boolean didPlayMove()
	{
		return this.playedMove;
	}
	public boolean isOnRestart()
	{
		return this.onRestart;
	}
	public boolean isOnChange()
	{
		return this.onChange;
	}
	public boolean isOnDifficulty()
	{
		return this.onDifficulty;
	}
	public boolean isOnMode()
	{
		return this.onMode;
	}
	public boolean didRestart()
	{
		return this.restarted;
	}
	public boolean didChange()
	{
		return this.changed;
	}
	public boolean didDifficultyChange()
	{
		return this.difficulty;
	}
	public boolean didModeChange()
	{
		return this.mode;
	}
	public boolean getShowStatistics()
	{
		return this.statistics;
	}
	public void setPlayMove(boolean playMove)
	{
		this.playedMove=playMove;
	}
	public void setRestarted(boolean restarted)
	{
		this.restarted=restarted;
	}
	public void setChanged(boolean changed)
	{
		this.changed=changed;
	}
	public void setDifficultyChanged(boolean difficulty)
	{
		this.difficulty=difficulty;
	}
	public void setModeChanged(boolean mode)
	{
		this.mode=mode;
	}
	public void setShowStatistics(boolean statistics)
	{
		this.statistics=statistics;
	}
}