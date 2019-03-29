package tictactoe.main;
import javax.swing.*;
public class TicTacToeWindow
{
	public TicTacToeWindow(TicTacToeCanvas canvas)
	{
		JFrame frame=new JFrame("Tic Tac Toe Engine");
		frame.add(canvas);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}
}