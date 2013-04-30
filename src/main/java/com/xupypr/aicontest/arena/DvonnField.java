package com.xupypr.aicontest.arena;

public class DvonnField
{
	private String[][] field = new String[11][5];

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		int c = 0;
		for (int y = 0; y < 5; y++)
		{
			for (int x = 0; x < 11 - Math.abs(y - 2); x++)
			{
				if (field[x][y] != null && !field[x][y].equals(""))
				{
					c++;
					sb.append((char) (97 + x)).append(y + 1).append(" ").append(field[x][y]).append("\n");
				}
			}
		}
		return c + "\n" + sb.toString();
	}
	
	@Override
	public DvonnField clone() {
		DvonnField result = new DvonnField();
		for (int y = 0; y < 5; y++)
		{
			for (int x = 0; x < 11 - Math.abs(y - 2); x++)
			{
				result.field[x][y] = this.field[x][y];
				System.out.println(x+" "+y+" "+field[x][y]);
			}
		}
		return result;
	}

	public boolean canMakeTurn(char color)
	{
		// только для фазы 2
		// Пробегаемся по всему полю
		for (int y = 0; y < 5; y++)
		{
			for (int x = 0; x < 11 - Math.abs(y - 2); x++)
			{
				// Найдём фишку своего цвета
				if (field[x][y] == null || field[x][y].equals(""))
				{
					continue;
				}
				if (field[x][y].charAt(field[x][y].length() - 1) != color)
				{
					continue;
				}
				// И она не окружена
				if (x > 0 && x < 10 - (Math.abs(y - 2)) && y > 0 && y < 4)
				{
					// Значит клетка не на краю - крайние всегда можем двигать
					if (y == 1 // нижний ряд
							&& field[x - 1][y] != null && !field[x - 1][y].equals("")
							&& field[x + 1][y] != null && !field[x + 1][y].equals("")
							&& field[x][y + 1] != null && !field[x][y + 1].equals("")
							&& field[x + 1][y + 1] != null && !field[x + 1][y + 1].equals("")
							&& field[x][y - 1] != null && !field[x][y - 1].equals("")
							&& field[x - 1][y - 1] != null && !field[x - 1][y - 1].equals(""))
					{
						continue;
					}
					if (y == 2 // серединка
							&& field[x - 1][y] != null && !field[x - 1][y].equals("")
							&& field[x + 1][y] != null && !field[x + 1][y].equals("")
							&& field[x][y + 1] != null && !field[x][y + 1].equals("")
							&& field[x - 1][y + 1] != null && !field[x - 1][y + 1].equals("")
							&& field[x][y - 1] != null && !field[x][y - 1].equals("")
							&& field[x - 1][y - 1] != null && !field[x - 1][y - 1].equals(""))
					{
						continue;
					}
					if (y == 3 // верхний ряд
							&& field[x - 1][y] != null && !field[x - 1][y].equals("")
							&& field[x + 1][y] != null && !field[x + 1][y].equals("")
							&& field[x][y + 1] != null && !field[x][y + 1].equals("")
							&& field[x - 1][y + 1] != null && !field[x - 1][y + 1].equals("")
							&& field[x][y - 1] != null && !field[x][y - 1].equals("")
							&& field[x + 1][y - 1] != null && !field[x + 1][y - 1].equals(""))
					{
						continue;
					}
				}
				// Пробуем сдвинуть в хотя бы в одном направлении
				int l = field[x][y].length();
				//ход влево
				if (x - l >= 0 && field[x - l][y] != null && !field[x - l][y].equals(""))
				{
					return true;
				}
				//ход вправо
				if (x + l < (11 - Math.abs(y - 2)) && field[x + l][y] != null && !field[x + l][y].equals(""))
				{
					return true;
				}
				//ход ввверх-вправо
				int dx = 0;
				int dy = 0;
				for (int i = 0; i < l; i++)
				{
					dy++;
					if (y + dy <= 2)
					{
						dx = dx + 1;
					} else
					{
						dx = dx + 0;
					}
				}
				if (Math.abs(dx) + Math.abs(dy) != 0 && x + dx >= 0 && x + dx < (11 - Math.abs(y + dy - 2))
						&& y + dy >= 0 && y + dy < 5 && field[x + dx][y + dy] != null
						&& !field[x + dx][y + dy].equals(""))
				{
					return true;
				}

				//ход вниз-вправо
				dx = 0;
				dy = 0;
				for (int i = 0; i < l; i++)
				{
					dy--;
					if (y + dy >= 2)
					{
						dx = dx + 1;
					} else
					{
						dx = dx + 0;
					}
				}
				if (Math.abs(dx) + Math.abs(dy) != 0 && x + dx >= 0 && x + dx < (11 - Math.abs(y + dy - 2))
						&& y + dy >= 0 && y + dy < 5 && field[x + dx][y + dy] != null
						&& !field[x + dx][y + dy].equals(""))
				{
					return true;
				}
				//ход ввверх-влево
				dx = 0;
				dy = 0;
				for (int i = 0; i < l; i++)
				{
					dy++;
					if (y + dy <= 2)
					{
						dx = dx + 0;
					} else
					{
						dx = dx - 1;
					}
				}
				if (Math.abs(dx) + Math.abs(dy) != 0 && x + dx >= 0 && x + dx < (11 - Math.abs(y + dy - 2))
						&& y + dy >= 0 && y + dy < 5 && field[x + dx][y + dy] != null
						&& !field[x + dx][y + dy].equals(""))
				{
					return true;
				}
				//ход вниз-влево
				dx = 0;
				dy = 0;
				for (int i = 0; i < l; i++)
				{
					dy--;
					if (y + dy >= 2)
					{
						dx = dx + 0;
					} else
					{
						dx = dx - 1;
					}
				}
				if (Math.abs(dx) + Math.abs(dy) != 0 && x + dx >= 0 && x + dx < (11 - Math.abs(y + dy - 2))
						&& y + dy >= 0 && y + dy < 5 && field[x + dx][y + dy] != null
						&& !field[x + dx][y + dy].equals(""))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void applyTurn(int phase, int turnCounter, char color, String turn)
	{
		if (phase == 1)
		{
			int x = Character.getNumericValue(turn.charAt(0)) - Character.getNumericValue('a');
			int y = Character.getNumericValue(turn.charAt(1)) - Character.getNumericValue('1');
			if (turnCounter <= 3)
			{
				field[x][y] = "R";
			} else
			{
				field[x][y] = "" + color;
			}
		} else
		{
			int x1 = Character.getNumericValue(turn.charAt(0)) - Character.getNumericValue('a');
			int y1 = Character.getNumericValue(turn.charAt(1)) - Character.getNumericValue('1');
			int x2 = Character.getNumericValue(turn.charAt(2)) - Character.getNumericValue('a');
			int y2 = Character.getNumericValue(turn.charAt(3)) - Character.getNumericValue('1');
			field[x2][y2] = new String(field[x2][y2] + field[x1][y1]);
			field[x1][y1] = null;
			// Пробегаемся по всему полю
			// Ищем dvonn-фишки
			// делаем поиск в ширину от dvonn-фишек
			// отсекаем 
			boolean[][] linked = new boolean[11][5];
			int[] xo = new int[55];
			int[] yo = new int[55];
			for (int y = 0; y < 5; y++)
			{
				for (int x = 0; x < 11 - Math.abs(y - 2); x++)
				{
					if (!linked[x][y] && field[x][y] != null && !field[x][y].equals("") && field[x][y].contains("R"))
					{
						linked[x][y] = true;
						int t = 0;
						int c = 0;
						xo[c] = x;
						yo[c] = y;
						while (c <= t)
						{
							// налево
							if (xo[c] - 1 >= 0 && !linked[xo[c] - 1][yo[c]] && field[xo[c] - 1][yo[c]] != null
									&& !field[xo[c] - 1][yo[c]].equals(""))
							{
								linked[xo[c] - 1][yo[c]] = true;
								t++;
								xo[t] = xo[c] - 1;
								yo[t] = yo[c];
							}
							// направо
							if (xo[c] + 1 < 11 - Math.abs(yo[c] - 2) && !linked[xo[c] + 1][yo[c]]
									&& field[xo[c] + 1][yo[c]] != null
									&& !field[xo[c] + 1][yo[c]].equals(""))
							{
								linked[xo[c] + 1][yo[c]] = true;
								t++;
								xo[t] = xo[c] + 1;
								yo[t] = yo[c];
							}
							// с изменением ряда
							if (yo[c] - 1 >= 0 && !linked[xo[c]][yo[c] - 1] && field[xo[c]][yo[c] - 1] != null
									&& !field[xo[c]][yo[c] - 1].equals(""))
							{
								linked[xo[c]][yo[c] - 1] = true;
								t++;
								xo[t] = xo[c];
								yo[t] = yo[c] - 1;
							}
							if (yo[c] + 1 < 5 && !linked[xo[c]][yo[c] + 1] && field[xo[c]][yo[c] + 1] != null
									&& !field[xo[c]][yo[c] + 1].equals(""))
							{
								linked[xo[c]][yo[c] + 1] = true;
								t++;
								xo[t] = xo[c];
								yo[t] = yo[c] + 1;
							}
							int dxup = (yo[c] >= 2) ? -1 : 1;
							int dxdown = (yo[c] <= 2) ? -1 : 1;
							if (yo[c] - 1 >= 0 && xo[c] + dxdown >= 0 && xo[c] + dxdown < 11 - Math.abs(yo[c] - 1 - 2)
									&& !linked[xo[c] + dxdown][yo[c] - 1] && field[xo[c] + dxdown][yo[c] - 1] != null
									&& !field[xo[c]+ dxdown][yo[c] - 1].equals(""))
							{
								linked[xo[c] + dxdown][yo[c] - 1] = true;
								t++;
								xo[t] = xo[c] + dxdown;
								yo[t] = yo[c] - 1;
							}
							if (yo[c] + 1 < 5 && xo[c] + dxup >= 0 && xo[c] + dxup < 11 - Math.abs(yo[c] + 1 - 2)
									&& !linked[xo[c] + dxup][yo[c] + 1] && field[xo[c]+ dxup][yo[c] + 1] != null
									&& !field[xo[c]+ dxup][yo[c] + 1].equals(""))
							{
								linked[xo[c] + dxup][yo[c] + 1] = true;
								t++;
								xo[t] = xo[c] + dxup;
								yo[t] = yo[c] + 1;
							}
							c++;
						}
					}
				}
			}
			// отсекаем все непомеченные
			for (int y = 0; y < 5; y++)
			{
				for (int x = 0; x < 11 - Math.abs(y - 2); x++)
				{
					if (!linked[x][y] && field[x][y] != null && !field[x][y].equals(""))
					{
						field[x][y] = new String("");
						System.out.println("CUT :" + x + " " + y);
					}
				}
			}
			linked = null;
		}
	}

	public boolean isTurnPossible(int phase, char color, String turn)
	{
		if (phase == 1)
		{
			// Фаза 1 - достаточно что поле пустое
			int x = Character.getNumericValue(turn.charAt(0)) - Character.getNumericValue('a');
			int y = Character.getNumericValue(turn.charAt(1)) - Character.getNumericValue('1');
			return (field[x][y] == null || field[x][y].equals(""));
		} else
		{
			// Фаза 2 - мы взяли поле своего цвета и переместили по разрешённому ходу на непустую ячейку
			int x1 = Character.getNumericValue(turn.charAt(0)) - Character.getNumericValue('a');
			int y1 = Character.getNumericValue(turn.charAt(1)) - Character.getNumericValue('1');
			if (field[x1][y1] == null || field[x1][y1].equals(""))
			{
				return false; // пытались сдвинуть пустую ячейку
			}
			if (field[x1][y1].charAt(field[x1][y1].length() - 1) != color)
			{
				return false; // хватанули не свою ячейку
			}
			// А может взяли окружённую ячейку
			if (x1 > 0 && x1 < 10 - (Math.abs(y1 - 2)) && y1 > 0 && y1 < 4)
			{
				// Значит клетка не на краю - крайние всегда можем двигать
				if (y1 == 1 // нижний ряд
						&& field[x1 - 1][y1] != null && !field[x1 - 1][y1].equals("")
						&& field[x1 + 1][y1] != null && !field[x1 + 1][y1].equals("")
						&& field[x1][y1 + 1] != null && !field[x1][y1 + 1].equals("")
						&& field[x1 + 1][y1 + 1] != null && !field[x1 + 1][y1 + 1].equals("")
						&& field[x1][y1 - 1] != null && !field[x1][y1 - 1].equals("")
						&& field[x1 - 1][y1 - 1] != null && !field[x1 - 1][y1 - 1].equals(""))
				{
					return false;
				}
				if (y1 == 2 // серединка
						&& field[x1 - 1][y1] != null && !field[x1 - 1][y1].equals("")
						&& field[x1 + 1][y1] != null && !field[x1 + 1][y1].equals("")
						&& field[x1][y1 + 1] != null && !field[x1][y1 + 1].equals("")
						&& field[x1 - 1][y1 + 1] != null && !field[x1 - 1][y1 + 1].equals("")
						&& field[x1][y1 - 1] != null && !field[x1][y1 - 1].equals("")
						&& field[x1 - 1][y1 - 1] != null && !field[x1 - 1][y1 - 1].equals(""))
				{
					return false;
				}
				if (y1 == 3 // верхний ряд
						&& field[x1 - 1][y1] != null && !field[x1 - 1][y1].equals("")
						&& field[x1 + 1][y1] != null && !field[x1 + 1][y1].equals("")
						&& field[x1][y1 + 1] != null && !field[x1][y1 + 1].equals("")
						&& field[x1 - 1][y1 + 1] != null && !field[x1 - 1][y1 + 1].equals("")
						&& field[x1][y1 - 1] != null && !field[x1][y1 - 1].equals("")
						&& field[x1 + 1][y1 - 1] != null && !field[x1 + 1][y1 - 1].equals(""))
				{
					return false;
				}
			}

			int x2 = Character.getNumericValue(turn.charAt(2)) - Character.getNumericValue('a');
			int y2 = Character.getNumericValue(turn.charAt(3)) - Character.getNumericValue('1');
			if (field[x2][y2] == null || field[x2][y2].equals(""))
			{
				return false; // пытались поставить на пустую ячейку
			}
			boolean found = false;
			int l = field[x1][y1].length();
			// пытаемся понять, в каком направлении был совершён ход (для проверки его корректности)
			//ход влево
			if (x1 - l == x2)
			{
				found = true;
			}
			//ход вправо
			if (x1 + l == x2)
			{
				found = true;
			}
			//ход ввверх-вправо
			int dx = 0;
			int dy = 0;
			for (int i = 0; i < l; i++)
			{
				dy++;
				if (y1 + dy <= 2)
				{
					dx = dx + 1;
				} else
				{
					dx = dx + 0;
				}
			}
			if (x1 + dx == x2 && y1 + dy == y2)
			{
				found = true;
			}
			//ход вниз-вправо
			dx = 0;
			dy = 0;
			for (int i = 0; i < l; i++)
			{
				dy--;
				if (y1 + dy >= 2)
				{
					dx = dx + 1;
				} else
				{
					dx = dx + 0;
				}
			}
			if (x1 + dx == x2 && y1 + dy == y2)
			{
				found = true;
			}
			//ход ввверх-влево
			dx = 0;
			dy = 0;
			for (int i = 0; i < l; i++)
			{
				dy++;
				if (y1 + dy <= 2)
				{
					dx = dx + 0;
				} else
				{
					dx = dx - 1;
				}
			}
			if (x1 + dx == x2 && y1 + dy == y2)
			{
				found = true;
			}
			//ход вниз-влево
			dx = 0;
			dy = 0;
			for (int i = 0; i < l; i++)
			{
				dy--;
				if (y1 + dy >= 2)
				{
					dx = dx + 0;
				} else
				{
					dx = dx - 1;
				}
			}
			if (x1 + dx == x2 && y1 + dy == y2)
			{
				found = true;
			}
			return found;
		}
	}

	public int whiteWon()
	{
		// Пробегаемся по всему полю и подсчитываем очки
		int w = 0;
		int b = 0;
		for (int y = 0; y < 5; y++)
		{
			for (int x = 0; x < 11 - Math.abs(y - 2); x++)
			{
				if (field[x][y] != null && !field[x][y].equals(""))
				{
					if (field[x][y].charAt(field[x][y].length() - 1) == 'W')
					{
						w += field[x][y].length();
					}
					if (field[x][y].charAt(field[x][y].length() - 1) == 'B')
					{
						b += field[x][y].length();
					}
				}
			}
		}
		if (w > b)
		{
			return 1;
		}
		if (w < b)
		{
			return -1;
		}
		return 0;
	}

	public String[][] getField()
	{
		return field;
	}

	public void setField(String[][] field)
	{
		this.field = field;
	}
}