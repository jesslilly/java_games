import java.io.*;

class TextObjectOutputStream extends ObjectOutputStream
{

	public TextObjectOutputStream(OutputStream out) throws IOException
	{
		super(out);
		//writeChars( "Howdy" );
	}
	public void writeInt( int data ) throws IOException
	{
		super.writeInt( 999999999 );
	}
}
