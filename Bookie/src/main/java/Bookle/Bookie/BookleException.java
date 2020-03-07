package Bookle.Bookie;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault
public class BookleException extends Exception {

	public BookleException(String msg, Throwable causa) {

		super(msg, causa);
	}

	public BookleException(String msg) {

		super(msg);
	}
}
