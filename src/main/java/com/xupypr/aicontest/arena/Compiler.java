package com.xupypr.aicontest.arena;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mongodb.DBObject;
import org.springframework.core.task.TaskExecutor;
import com.xupypr.aicontest.database.MongoConnector;

/**
 * Компилятор ботов
 * 
 * 1. Берёт очередного нескомпилированного бота
 * 2. Компилирует его
 * 3. Записывает результат компиляции обратно в базу
 */
public class Compiler implements Runnable
{
	public void run()
	{
		while (true)
		{
			// Берём нескомпилированного бота
			DBObject bot = null;
			do
			{
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				bot = mc.getUncompiledBot();
			} while (bot == null);

			System.out.println("COMPILE BOT " + bot.get("username") + " >> " + bot.get("language"));

			// Сохраняем бота
			File f;
			BufferedWriter writer;
			try
			{
				File path = new File("/tmp/" + bot.get("_id").toString());
				path.mkdirs();
				f = new File(path,
						"main"
								+ (bot.get("language").equals("PASCAL") ? ".pas" :
										bot.get("language").equals("JAVA") ? ".java" : bot.get("language").equals("CPP") ? ".cpp"
												: ".py"));
				f.createNewFile();
				writer = new BufferedWriter(new FileWriter(f));
				writer.write(bot.get("source").toString());
				writer.flush();
				writer.close();
				writer = null;
				f = null;
				path = null;
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}

			if (bot.get("language").equals("PASCAL"))
			{
				// Компилим бота
				try
				{
					// Компилим бота
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", "fpc -So -XS /tmp/" + bot.get("_id").toString()
							+ "/main.pas -o/tmp/" + bot.get("_id").toString() + "/bot.sh");
					pb.redirectErrorStream(true);
					Process pr = pb.start();
					BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					String line;
					while ((line = in.readLine()) != null)
					{
						System.out.println(line);
					}
					BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
					while ((line = err.readLine()) != null)
					{
						System.out.println(line);
					}
					mc.setBotCompileResult(bot.get("_id").toString(), bot.get("username").toString(),
							pr.waitFor() == 0 ? "SUCCESS" : "FAILURE");
					in.close();
					pb = null;
					in = null;
					pr = null;
				} catch (Exception e)
				{
					e.printStackTrace();
					mc.setBotCompileResult(bot.get("_id").toString(), bot.get("username").toString(), "FAILURE");
				}
			}
			if (bot.get("language").equals("JAVA"))
			{
				try
				{
					// Компилим бота
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", "javac -cp \".;*\" /tmp/" + bot.get("_id").toString()
							+ "/main.java");
					pb.redirectErrorStream(true);
					Process pr = pb.start();
					BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					String line;
					while ((line = in.readLine()) != null)
					{
						System.out.println(line);
					}
					BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
					while ((line = err.readLine()) != null)
					{
						System.out.println(line);
					}
					mc.setBotCompileResult(bot.get("_id").toString(), bot.get("username").toString(),
							pr.waitFor() == 0 ? "SUCCESS" : "FAILURE");
					in.close();
					pb = null;
					in = null;
					pr = null;
				} catch (Exception e)
				{
					e.printStackTrace();
					mc.setBotCompileResult(bot.get("_id").toString(), bot.get("username").toString(), "FAILURE");
				}
			}
			if (bot.get("language").equals("CPP"))
			{
				try
				{
					// Компилим бота
					ProcessBuilder pb = new ProcessBuilder("bash", "-c",
							"g++ -static -fno-optimize-sibling-calls -fno-strict-aliasing -lm -s -x c++ -O2 -o /tmp/"
									+ bot.get("_id").toString() + "/bot.sh /tmp/" + bot.get("_id").toString()
									+ "/main.cpp");
					pb.redirectErrorStream(true);
					Process pr = pb.start();
					BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					String line;
					while ((line = in.readLine()) != null)
					{
						System.out.println(line);
					}
					BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
					while ((line = err.readLine()) != null)
					{
						System.out.println(line);
					}
					mc.setBotCompileResult(bot.get("_id").toString(), bot.get("username").toString(),
							pr.waitFor() == 0 ? "SUCCESS" : "FAILURE");
					in.close();
					pb = null;
					in = null;
					pr = null;
				} catch (Exception e)
				{
					e.printStackTrace();
					mc.setBotCompileResult(bot.get("_id").toString(), bot.get("username").toString(), "FAILURE");
				}
			}
			if (bot.get("language").equals("PYTHON"))
			{
				// Не надо компилить
				mc.setBotCompileResult(bot.get("_id").toString(), bot.get("username").toString(), "SUCCESS");
			}
			bot = null;
		}
	}

	private static Compiler compiler;
	private static MongoConnector mc;

	public synchronized static Compiler newInstance(TaskExecutor taskExecutor)
	{
		if (compiler == null)
		{
			compiler = new Compiler();
			mc = new MongoConnector();
			taskExecutor.execute(compiler);
		}
		return compiler;
	}
}