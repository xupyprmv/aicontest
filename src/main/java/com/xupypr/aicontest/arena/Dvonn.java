package com.xupypr.aicontest.arena;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBObject;
import org.springframework.core.task.TaskExecutor;
import com.xupypr.aicontest.database.MongoConnector;

/**
 * Арена для ботов Dvonn
 * 
 * 1. Извлекаем последние игры ботов и рейтинг ботов
 * 
 * 2. Берём бота который играл раньше всех по времени или не играл вовсе, 
 * 	  но при этом не привысыл общее кол-во игр и выбирает ему оппонента по тому же принципу
 *    (кол-во игр с данным оппонентом тоже не должно быть превышено).
 * 
 * 3. Проводим бой
 * 3.1 Выбираем кто будет ходить первым (по статистике боёв ботов)
 * 3.2 Проводим первую фазу (изначально поле пустое)
 *   3.2.1 Отправляем первому поле
 *   3.2.2 Получаем ответ, проверяем что поле не занято - ставит фишку (если ход некорректен или превышены ресурсы - техническое поражение)
 *   3.2.3 Отправляет второму поле
 *   3.2.4 Получаем ответ, проверяем что поле не занято - ставит фишку (если ход некорректен или превышены ресурсы - техническое поражение)
 *   3.2.5 Если ещё не все фишки расствлены - переход к 3.2.1
 * 
 * 3.3 Как только всё поле расставлено - переход к фазе 2. Изначально ходит первый бот.
 *   3.3.1 Проверяет что текущему боту можно ходить, если нет, проверяет можно ли ходить боту оппоненту
 *         Если не могут ходить оба - конец боя. Если кто-то может ходить - ему передаётся ход.
 *   3.3.2 Боту отправляется игровое поле
 *   3.3.3 Получеам ответ, проверяем корректность хода (если ход некорректен или превышены ресурсы - техническое поражение)
 *   3.3.4 Производим перенос фишки (обновляем игровое поле).
 *   3.3.5 Ход передаётся боту оппоненту
 *   3.3.6 Переход к 3.3.1
 *   
 * 4. Записать все ответы программ в журнал (с указанием цвета и хода)
 */

public class Dvonn implements Runnable
{
	public void run()
	{
		while (true)
		{
			// Берём нескомпилированного бота
			List<DBObject> bots = new ArrayList<DBObject>();
			DBObject bot1 = null;
			DBObject bot2 = null;
			do
			{
				try
				{
					Thread.sleep(100); //TODO /10
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				// Выбираем бота которы должен сыграть с наивысшим преоритетом
				bots = mc.getBotsForFight();
			} while (bots == null);

			bot1 = bots.get(0);
			bot2 = bots.get(1);

			System.out.println(new StringBuffer().append("FIGHT: BOT1 > ").append(bot1.get("username")).append(" [")
					.append(bot1.get("language")).append(" ]").append(" BOT2 > ").append(bot2.get("username")).append(" [")
					.append(bot2.get("language")).append(" ]"));
			/*
			 * 3. Проводим бой
			 * 3.1 Выбираем кто будет ходить первым (по статистике боёв ботов)
			 * 3.2 Проводим первую фазу (изначально поле пустое)
			 *   3.2.1 Отправляем первому поле
			 *   3.2.2 Получаем ответ, проверяем что поле не занято - ставит фишку (если ход некорректен или превышены ресурсы - техническое поражение)
			 *   3.2.3 Отправляет второму поле
			 *   3.2.4 Получаем ответ, проверяем что поле не занято - ставит фишку (если ход некорректен или превышены ресурсы - техническое поражение)
			 *   3.2.5 Если ещё не все фишки расствлены - переход к 3.2.1
			 * 
			 * 3.3 Как только всё поле расставлено - переход к фазе 2. Изначально ходит первый бот.
			 *   3.3.1 Проверяет что текущему боту можно ходить, если нет, проверяет можно ли ходить боту оппоненту
			 *         Если не могут ходить оба - конец боя. Если кто-то может ходить - ему передаётся ход.
			 *   3.3.2 Боту отправляется игровое поле
			 *   3.3.3 Получеам ответ, проверяем корректность хода (если ход некорректен или превышены ресурсы - техническое поражение)
			 *   3.3.4 Производим перенос фишки (обновляем игровое поле).
			 *   3.3.5 Ход передаётся боту оппоненту
			 *   3.3.6 Переход к 3.3.1
			 */
			DvonnField field = new DvonnField();
			int h = 0;
			String game = "";
			try
			{

				for (DBObject bot : bots)
				{
					if (bot.get("language").equals("PASCAL"))
					{
						bot.put("path", "/tmp/" + bot.get("_id").toString() + "/bot.sh");
					}
					if (bot.get("language").equals("JAVA"))
					{
						bot.put("path", "java -cp /tmp/" + bot.get("_id").toString() + "/ main");
					}
					if (bot.get("language").equals("CPP"))
					{
						bot.put("path", "/tmp/" + bot.get("_id").toString() + "/bot.sh");
					}
					if (bot.get("language").equals("PYTHON"))
					{
						bot.put("path", "python /tmp/" + bot.get("_id").toString() + "/main.py");
					}
				}
				// фаза 1
				for (int i = 0; i < 49; i++)
				{
					DBObject bot;
					bot = bots.get(h);
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", bot.get("path").toString());
					pb.redirectErrorStream(true);
					Process pr = pb.start();
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
					// Отправляем боту данные
					out.write("1\n");
					out.write(h == 0 ? "W\n" : "B\n");
					out.write(field.toString());
					out.flush();
					BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					String line;
					while ((line = in.readLine()) != null)
					{
						System.out.println(">" + line);
						// Получаем ответ
						if (line != null && !line.equals("") && field.isTurnPossible(1, h == 0 ? 'W' : 'B', line))
						{
							field.applyTurn(1, i + 1, h == 0 ? 'W' : 'B', line);
							game += (h == 0 ? 'W' : 'B') + line + " ";
						} else
						{
							if (line == null || line.equals(""))
							{
								line = "N/A";
							}
							// дисквалификация по неправильному ходу
							game += (h == 0 ? 'W' : 'B') + line + " ";
							throw new IllegalStateException("IMPOSSIBLE TURN " + line);
						}
					}
					BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
					while ((line = err.readLine()) != null)
					{
						System.out.println(line);
					}
					in.close();
					pb = null;
					in = null;
					out = null;
					pr = null;
					h = 1 - h;
				}
				// фаза 2
				h = 0;
				int counter = 49;
				boolean end = false;
				do
				{
					if (!field.canMakeTurn(h == 0 ? 'W' : 'B'))
					{
						h = 1 - h;
						if (!field.canMakeTurn(h == 0 ? 'W' : 'B'))
						{
							end = true;
						}
					}
					if (!end)
					{
						// делаем ход
						DBObject bot;
						bot = bots.get(h);
						ProcessBuilder pb = new ProcessBuilder("bash", "-c", bot.get("path").toString());
						pb.redirectErrorStream(true);
						Process pr = pb.start();
						BufferedWriter out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
						// Отправляем боту данные
						out.write("2\n");
						out.write(h == 0 ? "W\n" : "B\n");
						out.write(field.toString());
						out.flush();
						BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
						String line;
						while ((line = in.readLine()) != null)
						{
							System.out.println(line);
							// Получаем ответ
							game += (h == 0 ? 'W' : 'B') + line + " ";
							if (line != null && !line.equals("") && field.isTurnPossible(2, h == 0 ? 'W' : 'B', line))
							{
								counter++;
								field.applyTurn(2, counter, h == 0 ? 'W' : 'B', line);
							} else
							{
								if (line == null || line.equals(""))
								{
									line = "N/A";
								}
								// дисквалификация по неправильному ходу
								throw new IllegalStateException("IMPOSSIBLE TURN " + line);
							}
						}
						BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
						while ((line = err.readLine()) != null)
						{
							System.out.println(line);
						}
						in.close();
						pb = null;
						in = null;
						out = null;
						pr = null;
						h = 1 - h;
					}
				} while (!end);

				switch (field.whiteWon())
				{
				case 1:
					mc.createEvent(bot1.get("_id").toString(), bot2.get("_id").toString(), "win",
							bot1.get("username") + " выиграл бота " + bot2.get("username"), game + " (" + counter + ")");
					break;
				case -1:
					mc.createEvent(bot1.get("_id").toString(), bot2.get("_id").toString(), "loss",
							bot1.get("username") + " проиграл боту " + bot2.get("username"), game + " (" + counter + ")");
					break;
				case 0:
					mc.createEvent(bot1.get("_id").toString(), bot2.get("_id").toString(), "draw", bot1.get("username")
							+ " сыграл в ничью с ботом " + bot2.get("username"), game + " (" + counter + ")");
					break;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				// Дисквалификация по ошибке
				if (h == 0)
				{
					mc.createEvent(bot1.get("_id").toString(), bot2.get("_id").toString(), "loss",
							bot1.get("username") + " проиграл боту " + bot2.get("username") + " (Техническое поражение)", game);
				} else
				{
					mc.createEvent(bot1.get("_id").toString(), bot2.get("_id").toString(), "win",
							bot1.get("username") + " выиграл бота " + bot2.get("username") + " (Техническая победа)", game);
				}
			}
		}
	}

	private static Dvonn dvonn;
	private static MongoConnector mc;

	public synchronized static Dvonn newInstance(TaskExecutor taskExecutor)
	{
		if (dvonn == null)
		{
			mc = new MongoConnector();
			dvonn = new Dvonn();
			taskExecutor.execute(dvonn);
		}
		return dvonn;
	}
}