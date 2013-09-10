package com.xupypr.aicontest.database;

import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.xupypr.aicontest.arena.DvonnField;

/**
 * MongoDB
 * 
 * Database 	:	- aicontest
 * Collections	: 	- user // пользователь
 * 					- bot // бот
 * 					- event // событие
 * 					- session // сессия
 */
public class MongoConnector
{
	private static MongoClient mongo;

	public MongoConnector()
	{
		if (mongo == null)
		{
			try
			{
				mongo = new MongoClient();
			} catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
		}
	}

	/* AUTH BLOCK [START] */
	public String addSessionKeyToUser(String userId)
	{
		if (userId == null || "".equals(userId))
		{
			return null;
		}
		WriteResult result = null;
		result = mongo.getDB("aicontest").getCollection("session").remove(new BasicDBObject("userid", userId));
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("cann't remove old session : " + result.getError());
		}

		String key = UUID.randomUUID().toString();
		BasicDBObject doc = new BasicDBObject()
				.append("skey", key).append("userid", userId);
		result = mongo.getDB("aicontest").getCollection("session").insert(doc);
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("cann't add session : " + result.getError());
			return null;
		}
		return key;
	}

	public String getUserIdBySessionKey(String key)
	{
		if (key == null || "".equals(key))
		{
			return null;
		} else
		{
			DBObject object = mongo.getDB("aicontest").getCollection("session").findOne(new BasicDBObject("skey", key));
			return object.get("userid").toString();
		}
	}

	/* AUTH BLOCK [END] */

	/* EVENT BLOCK [START] */
	/**
	 * Создать событие
	 * 
	 * @param botUID1 - идентификатор бота
	 * @param botUID2 - идентификатор бота
	 * @param type - тип события
	 * @param comment - комментарий
	 * @param game - идентификатор игры
	 * @return String - идентификатор сохранённого объекта MongoDB
	 */
	public String createEvent(String botUID1, String botUID2, String type, String comment, String game)
	{
		// Сохранение события
		BasicDBObject doc = new BasicDBObject()
				.append("bot1", botUID1)
				.append("bot2", botUID2)
				.append("type", type)
				.append("comment", comment)
				.append("sdate", new Date())
				.append("game", game);
		WriteResult result = mongo.getDB("aicontest").getCollection("event").insert(doc);
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity insertion error : " + result.getError());
			return null;
		}
		return doc.getString("_id");
	}

	/**
	 * Извлечь событие по идентификатору
	 * 
	 * @param uid - идентификатор события
	 * @return DBObject - событие в виде объекта MongoDB
	 */
	public DBObject getEventByUID(String uid)
	{
		return mongo.getDB("aicontest").getCollection("event").findOne(new BasicDBObject("_id", new ObjectId(uid)));
	}

	public Map<String, DvonnField> getBotFight(String id)
	{
		DBObject event = getEventByUID(id);
		String game = event.get("game").toString();
		int l = game.length();
		int i = 0;
		String turn = "";
		int turnc = 1;
		DvonnField field = new DvonnField();
		Map<String, DvonnField> result = new LinkedHashMap<String, DvonnField>();
		while (i < l)
		{
			if (game.charAt(i) == ' ')
			{
				if (turn.length() == 3)
				{
					field.applyTurn(1, turnc, turn.charAt(0), turn.substring(1));
				}
				if (turn.length() == 5)
				{
					field.applyTurn(2, turnc, turn.charAt(0), turn.substring(1));
				}
				result.put(turn, field.clone());
				turn = "";
				turnc++;
			} else
			{
				turn += game.charAt(i);
			}
			i++;
		}
		return result;
	}

	/**
	 * Получить все события арены
	 * @return список объектов MongoDB
	 */
	public List<DBObject> getAllArenaEvents()
	{
		DBCursor cursor = mongo.getDB("aicontest").getCollection("event")
				.find().sort(new BasicDBObject("sdate", -1));
		return cursor.toArray();
	}

	/**
	 * Получить событие арены для пользователя
	 * @param userUID - идентификатор пользователя
	 * @return список объектов MongoDB
	 */
	public List<DBObject> getArenaEvents(String userUID)
	{
		DBObject bot = getBotByUserUID(userUID);
		if (bot == null)
		{
			return new ArrayList<DBObject>(0);
		}
		DBObject clause1 = new BasicDBObject("bot1", bot.get("_id").toString());
		DBObject clause2 = new BasicDBObject("bot2", bot.get("_id").toString());
		BasicDBList or = new BasicDBList();
		or.add(clause1);
		or.add(clause2);
		DBCursor cursor = mongo.getDB("aicontest").getCollection("event")
				.find(new BasicDBObject("$or", or)).sort(new BasicDBObject("sdate", -1));
		return cursor.toArray();
	}

	/**
	 * Удалить все события связанные с ботом
	 * @param botUID - идентификатор бота
	 */
	public void deleteEventsByBotId(String botUID)
	{
		WriteResult result = null;
		result = mongo.getDB("aicontest").getCollection("event").remove(new BasicDBObject("bot1", botUID));
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity remove error : " + result.getError());
		}
		result = mongo.getDB("aicontest").getCollection("event").remove(new BasicDBObject("bot2", botUID));
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity remove error : " + result.getError());
		}
	}

	/**
	 * Сортировщик рейтинга
	 */
	private Comparator<DBObject> ratingComparator = new Comparator<DBObject>()
	{

		public int compare(DBObject o1, DBObject o2)
		{
			if (Integer.parseInt(o1.get("points").toString()) > Integer.parseInt(o2.get("points").toString()))
				return -1;
			if (Integer.parseInt(o1.get("points").toString()) < Integer.parseInt(o2.get("points").toString()))
				return 1;
			return 0;
		}
	};
	private long count;

	/**
	 * Получить рейтинг
	 * 
	 * @return список объектов MongoDB
	 */
	public List<DBObject> getRating()
	{
		// TODO кэширование
		List<DBObject> result = new ArrayList<DBObject>();
		// запрашиваем всех ботов
		DBCursor cursor = mongo.getDB("aicontest").getCollection("bot").find(new BasicDBObject("compiled", "SUCCESS"));
		while (cursor.hasNext())
		{
			DBObject bot = cursor.next();
			DBObject clause1 = new BasicDBObject("bot1", bot.get("_id").toString());
			DBObject clause2 = new BasicDBObject("bot2", bot.get("_id").toString());
			BasicDBList or = new BasicDBList();
			or.add(clause1);
			or.add(clause2);
			DBCursor botEvents = mongo.getDB("aicontest").getCollection("event").find(new BasicDBObject("$or", or));
			int w = 0;
			int d = 0;
			int l = 0;
			Date lfdate = new Date(0);
			while (botEvents.hasNext())
			{
				DBObject event = botEvents.next();
				if (((Date) event.get("sdate")).after(lfdate))
				{
					lfdate = (Date) event.get("sdate");
				}
				if (event.get("type").equals("win"))
				{
					if (event.get("bot1").toString().equals(bot.get("_id").toString()))
					{
						w++;
					} else
					{
						l++;
					}
				}
				if (event.get("type").equals("loss"))
				{
					if (event.get("bot1").toString().equals(bot.get("_id").toString()))
					{
						l++;
					} else
					{
						w++;
					}
				}
				if (event.get("type").equals("draw"))
					d++;
			}
			bot.put("lfdate", lfdate);
			bot.put("wins", w);
			bot.put("loses", l);
			bot.put("draws", d);
			bot.put("games", w + d + l);
			bot.put("points", w * 3 + d);
			result.add(bot);
		}
		Collections.sort(result, ratingComparator);
		return result;
	}

	/* EVENT BLOCK [END] */

	/* BOT BLOCK [START] */
	public String createBot(String userUID, String language, String source)
	{
		// Найти всех предыдущих ботов пользователя
		DBCursor cursor = mongo.getDB("aicontest").getCollection("bot").find(new BasicDBObject("user", userUID));
		System.out.println("*** >" + userUID);
		while (cursor.hasNext())
		{
			// И удалить события у этих ботов
			DBObject bot = cursor.next();
			deleteEventsByBotId(bot.get("_id").toString());
		}

		// Удаление предыдущих ботов пользователя
		mongo.getDB("aicontest").getCollection("bot").remove(new BasicDBObject("user", userUID));

		// Получение имени пользователя
		DBObject user = getUserByUID(userUID);
		String username = user.get("login") + " (" + user.get("uname") + ", " + user.get("grade") + " класс)";

		// Сохранение бота
		BasicDBObject doc = new BasicDBObject()
				.append("language", language)
				.append("source", source)
				.append("user", userUID)
				.append("username", username)
				.append("sdate", new Date())
				.append("compiled", "not");
		WriteResult result = mongo.getDB("aicontest").getCollection("bot").insert(doc);
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity insertion error : " + result.getError());
			return null;
		}
		createEvent(doc.getString("_id"), null, "created", "Бот " + username + " был отправлен на арену.", null);
		return doc.getString("_id");
	}

	public DBObject getBotByUID(String uid)
	{
		return mongo.getDB("aicontest").getCollection("bot").findOne(new BasicDBObject("_id", new ObjectId(uid)));
	}

	public DBObject getBotByUserUID(String userUID)
	{
		return mongo.getDB("aicontest").getCollection("bot").findOne(new BasicDBObject("user", userUID));
	}

	public void deleteBotByUID(String uid)
	{
		// Удаление предыдущих событий бота
		deleteEventsByBotId(uid);

		WriteResult result = mongo.getDB("aicontest").getCollection("bot").remove(new BasicDBObject("_id", new ObjectId(uid)));
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity insertion error : " + result.getError());
		}
	}

	public void recompileBotByUID(String uid)
	{
		// Удаление предыдущих событий бота
		deleteEventsByBotId(uid);

		WriteResult result = mongo
				.getDB("aicontest")
				.getCollection("bot")
				.update(new BasicDBObject("_id", new ObjectId(uid)),
						new BasicDBObject("$set", new BasicDBObject("compiled", "not")));
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity update error : " + result.getError());
		}
		DBObject bot = getBotByUID(uid);
		createEvent(uid, null, "created", "Бот " + bot.get("username") + " был перекомпилирован.", null);
	}

	public DBObject getUncompiledBot()
	{
		DBCursor cursor = mongo.getDB("aicontest").getCollection("bot").find(new BasicDBObject("compiled", "not"))
				.sort(new BasicDBObject("sdate", 1)).limit(1);
		if (cursor.hasNext())
		{
			return cursor.next();
		} else
		{
			return null;
		}
	}

	public List<DBObject> getBotsForFight()
	{
		int fights = 3;
		List<DBObject> rating = getRating();
		List<DBObject> result = new ArrayList<DBObject>();
		Date lf = new Date();
		DBObject bot1 = null;
		// Выбираем первого бота - который раньше всех играл но ещё не доиграл 
		for (DBObject bot : rating)
		{
			if (((Date) bot.get("lfdate")).before(lf) && ((Integer) bot.get("games")) < fights * 2 * (rating.size() - 1)
					&& bot.get("compiled").toString().equals("SUCCESS"))
			{
				lf = (Date) bot.get("lfdate");
				bot1 = bot;
			}
		}
		if (bot1 == null)
		{
			return null;
		}
		// Нужно найти боту соперника (TODO оптимизировать)
		Random r = new Random();
		for (int i = 1; i < rating.size() * 2; i++)
		{
			DBObject bot = rating.get(r.nextInt(rating.size()));
			if (!bot.get("_id").toString().equals(bot1.get("_id").toString())
					&& ((Integer) bot.get("games")) < fights * 2 * (rating.size() - 1)
					&& bot.get("compiled").toString().equals("SUCCESS"))
			{
				count = mongo.getDB("aicontest").getCollection("event")
						.count(new BasicDBObject("bot1", bot1.get("_id").toString()).append("bot2", bot.get("_id").toString()));
				if (count < fights)
				{
					result.add(bot1);
					result.add(bot);
					return result;
				}
				count = mongo.getDB("aicontest").getCollection("event")
						.count(new BasicDBObject("bot2", bot1.get("_id").toString()).append("bot1", bot.get("_id").toString()));
				if (count < fights)
				{
					result.add(bot);
					result.add(bot1);
					return result;
				}
			}
		}
		return null;
	}

	public void setBotCompileResult(String uid, String username, String compileResult)
	{
		WriteResult result = mongo
				.getDB("aicontest")
				.getCollection("bot")
				.update(new BasicDBObject("_id", new ObjectId(uid)),
						new BasicDBObject("$set", new BasicDBObject("compiled", compileResult)));
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity update error : " + result.getError());
		}
		if (compileResult.equals("SUCCESS"))
		{
			createEvent(uid, null, "compile-success", "Бот " + username + " успешно прошёл фазу компиляции.", null);
		} else if (compileResult.equals("FAILURE"))
		{
			createEvent(uid, null, "compile-failure", "Бот " + username + " провалил фазу компиляции и снят с арены.", null);
		} else
		{
			createEvent(uid, null, "compile-failure", "Бот был переведён в состояние " + compileResult + ".", null);
		}
	}

	/* BOT BLOCK [END] */

	/* USER BLOCK [START] */
	/**
	 * @param uname
	 * @param grade
	 * @param login
	 * @param passwd
	 * @return UID or null
	 */
	public String createUser(String uname, String grade, String login, String passwd)
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		}
		BasicDBObject doc = new BasicDBObject()
				.append("uname", uname)
				.append("grade", grade).append("login", login)
				.append("passwd", new String(md.digest(("ai" + passwd + "contester").getBytes())));
		WriteResult result = mongo.getDB("aicontest").getCollection("user").insert(doc);
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity insertion error : " + result.getError());
			return null;
		}
		return doc.getString("_id");
	}

	public void deleteUser(String login)
	{
		WriteResult result = mongo.getDB("aicontest").getCollection("user").remove(new BasicDBObject("login", login));
		if (result.getError() != null && !result.getError().equals(""))
		{
			System.out.println("entity insertion error : " + result.getError());
		}
	}

	public boolean checkUserExistsByLogin(String login)
	{
		DBCursor cursor = mongo.getDB("aicontest").getCollection("user").find(new BasicDBObject("login", login));
		while (cursor.hasNext())
		{
			return true;
		}
		return false;
	}

	public DBObject getUserByUID(String uid)
	{
		return mongo.getDB("aicontest").getCollection("user").findOne(new BasicDBObject("_id", new ObjectId(uid)));
	}

	/**
	 * @param login
	 * @param passwd
	 * @return User UID or null
	 */
	public String loginUser(String login, String passwd)
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		}
		DBCursor cursor = mongo
				.getDB("aicontest")
				.getCollection("user")
				.find(new BasicDBObject("login", login)
						.append("passwd", new String(md.digest(("ai" + passwd + "contester").getBytes()))));
		while (cursor.hasNext())
		{
			return cursor.next().get("_id").toString();
		}
		return null;
	}
	/* USER BLOCK [END] */
}
