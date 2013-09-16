package com.sparkyland.spartique.gui;

import com.sparkyland.spartique.common.DebugLog;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.awt.*;

/////////////////////////////////////////////////////////////////
public class WPopupEntry extends Object implements MouseListener
{
	private String title, question, answer;
	private TextField field;
	private Dialog popup;

	public WPopupEntry ( String title, String question, Frame parentFrame )
	{
		this.question = question;
		this.title = title;
		this.answer = "AAA";
		popup = new Dialog( parentFrame, this.title, true );
		popup.setLayout( new FlowLayout() );
		Label label = new Label( this.question );
		field = new TextField( this.answer, 30 );
		field.selectAll();
		Button okButton = new Button( "OK" );
		okButton.addMouseListener(this);

		popup.setResizable( false );
		popup.add( label );
		popup.add( field );
		popup.add( okButton );
		popup.pack();
		popup.setLocation( 100,100);
		popup.setSize( 320, 100 );
	}

	public void setQuestion( String q ) { this.question = q; }
	public String getAnswer() { return this.answer; }

	public void popUp()
	{
		popup.show();
	}

	public void close()
	{
		popup.dispose();
	}

	public void mousePressed( MouseEvent e )
	{
		DebugLog.println("mousePressed(" + e + ")");
	}
	public void mouseReleased(MouseEvent e) 
	{
		DebugLog.println("mouseReleased(" + e + ")");
	}
	public void mouseClicked(MouseEvent e) 
	{
		DebugLog.println("mouseClicked(" + e + ")");
		this.answer = field.getText();
		this.close();
	}
	public void mouseEntered( MouseEvent e )
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}



}