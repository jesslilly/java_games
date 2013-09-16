import java.awt.*;
import java.io.*;
import java.awt.event.*;

class ObjectOutputReader
{
	static Frame frame;
	static TextArea textArea;
	static Button cancel;

	public static void main(String[] args) 
	{
		frame = new Frame( "ObjectOutputReader" );
		textArea = new TextArea("",20,50);
		cancel = new Button("Close");

		frame.add(textArea);
		frame.add("South", cancel );
		frame.pack();
		frame.show();

	
		FileDialog f = new FileDialog( frame, "Open", FileDialog.LOAD );
		f.show();
		String fileName = f.getFile();
		if (fileName != null)
		{
			try
			{
				FileInputStream fis = new FileInputStream( fileName );
				InputStreamReader isr = new InputStreamReader( fis );
				BufferedReader in = new BufferedReader( isr );

				String line;
				do
				{
					line = in.readLine();
					if (line == null)
					{
						break;
					}
					textArea.appendText( line );
				}
				while (line != null);
				in.close();
			}

			catch ( IOException e)
			{
				System.out.println(e);
			}
		}
	}

    public boolean action(Event e, Object arg)
	{
		System.out.println(e.target);
		if ( e.target ==  cancel )
		{
			System.out.println("Close.");
			frame.setVisible(false);
			frame.dispose();
		}
		return true;
	}
}
