//package v12;


import java.awt.*;

/////////////////////////////////////////////////////////////////
class WTextArea extends TextArea {

  public WTextArea() {
	  super();
	  setFont(new Font("Helvetica",Font.PLAIN,12));
	  //black blue cyan darkGray gray green lightGray magenta orange pink red white yellow
	  // It would be nice to make it auto- word wrap.
	  setBackground(Color.black);
	  setForeground(Color.orange);
  }

}

