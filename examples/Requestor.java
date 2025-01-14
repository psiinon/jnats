import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Constants;
import io.nats.client.Message;
import io.nats.client.NATSException;
import io.nats.client.Statistics;

public class Requestor {
	Map<String, String> parsedArgs = new HashMap<String, String>();

	int count = 20000;
	String url = Constants.DEFAULT_URL;
	String subject = "foo";
	byte[] payload = null;
	long start, end, elapsed;

	public void run(String[] args)
	{
		parseArgs(args);
		banner();

		ConnectionFactory cf = null;
		Connection c = null;

		try {
			cf = new ConnectionFactory(url);
			c = cf.createConnection();
		} catch (NATSException e) {
			System.err.println("Couldn't connect: " + e.getCause());
			System.exit(-1);
		}

		start = System.nanoTime();

		Message replyMsg = null;
		try {
			for (int i = 0; i < count; i++)
			{
				replyMsg = c.request(subject, payload);
				System.out.print("Got reply: " + new String(replyMsg.getData()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		end = System.nanoTime();
		elapsed = TimeUnit.NANOSECONDS.toSeconds(end-start);

		System.out.printf("Completed %d requests in %d seconds ", count, elapsed);
		if (elapsed > 0) {
		System.out.printf("(%d msgs/second).\n",
				(count / elapsed));
		} else {
			System.out.println();
			System.out.println("Test not long enough to produce meaningful stats. "
					+ "Please increase the message count (-count n)");
		}
		printStats(c);

	}

	private void printStats(Connection c)
	{
		Statistics s = c.getStats();
		System.out.printf("Statistics:  ");
		System.out.printf("   Incoming Payload Bytes: %d\n", s.getInBytes());
		System.out.printf("   Incoming Messages: %d\n", s.getInMsgs());
		System.out.printf("   Outgoing Payload Bytes: %d\n", s.getOutBytes());
		System.out.printf("   Outgoing Messages: %d\n", s.getOutMsgs());
	}

	private void usage()
	{
		System.err.println(
				"Usage:  Requestor [-url url] [-subject subject] " +
				"-count [count] [-payload payload]");

		System.exit(-1);
	}

	private void parseArgs(String[] args)
	{
		if (args == null)
			return;

		for (int i = 0; i < args.length; i++)
		{
			if (i + 1 == args.length)
				usage();

			parsedArgs.put(args[i], args[i + 1]);
			i++;
		}

		if (parsedArgs.containsKey("-count"))
			count = Integer.parseInt(parsedArgs.get("-count"));

		if (parsedArgs.containsKey("-url"))
			url = parsedArgs.get("-url");

		if (parsedArgs.containsKey("-subject"))
			subject = parsedArgs.get("-subject");

		if (parsedArgs.containsKey("-payload"))
			payload = parsedArgs.get("-payload").getBytes(Charset.forName("UTF-8"));
	}

	private void banner()
	{
		System.out.printf("Sending %d requests on subject %s\n",
				count, subject);
		System.out.printf("  URL: %s\n", url);
		System.out.printf("  Payload is %d bytes.\n",
				payload != null ? payload.length : 0);
	}

	public static void main(String[] args)
	{
		try
		{
			new Requestor().run(args);
		}
		catch (Exception ex)
		{
			System.err.println("Exception: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			System.exit(0);
		}

	}
}
