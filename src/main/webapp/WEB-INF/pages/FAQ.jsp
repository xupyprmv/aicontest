<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/header.jsp"%>

<div class="container">
	<ol>
		<li>
			<h3>Какие языки программирования/компиляторы поддерживаются?</h3>
			<p>
			<ol>
				<li>Pascal / Free Pascal 2.4.4 (fpc -So -XS %1 -o%name%.sh)</li>
				<li>Java / OpenJDK 1.6.0.24 (javac -cp ".;*" %1)</li>
				<li>C++ / GNU C++ 4.6.3 (g++ -static
					-fno-optimize-sibling-calls -fno-strict-aliasing -lm -s -x c++ -O2
					-o %name%.sh %1)</li>
				<li>Python / Python 2.7.3 (python -O %s)</li>
			</ol>
			</p>
		</li>
		<li>
			<h3>Откуда считывать входные данные и куда записывать результат?</h3>
			<p>Стандартный поток ввода/вывода (консоль).</p>
		</li>
		<li>
			<h3>Что произойдёт если мой бот выдаст некорректный ответ?</h3>
			<p>Бот будет считаться проигравшим в текущем сражении
				(техническое поражение) в случае выдачи некорректного ответа (ход не
				является допустимым, либо формат вывода не соответствует
				ограничениям, либо происходит ошибка времени выполнения).</p>
		</li>
		<li>
			<h3>Что произойдёт если мой бот израсходует больше ресурсов чем
				позволено?</h3>
			<p>
				На принятие решения вашему боту будет предоставлено <b>64 Мб
					оперативной памяти и 1 секунда</b>. При первышении лимита времени или
				памяти ваш бот будет считаться проигравшим в текущем сражении
				(техническое поражение).
			</p>
		</li>
		<li>
			<h3>Что произойдёт если мой бот не скомпилируется на сервере?</h3>
			<p>Ваш бот будет дисквалифицирован и не будет участвовать в
				соревнованиях на арене. Исправляйте ошибки и отправляйте бота
				заново. При необходимости обратитесь к жюри.</p>
		</li>
		<li>
			<h3>Есть ли ограничение на используемые ресурсы?</h3>
			<p>Да. Ваша программа не должна использовать файловую систему,
				возможности сети, прямое обращение к памяти в диапазоне, не
				предназначенном для вашей программы.</p>
		</li>
		<li>
			<h3>Можно ли отсылать несколько ботов одновременно?</h3>
			<p>Нет, при отправке на арену бота, предыдущий выставленный на
				арену бот автоматически исключается из соревнования.</p>
		</li>
		<li>
			<h3>Сколько сражений проведёт мой бот на арене?</h3>
			<p>С каждым ботом-соперником будет проведено 6 сражений. 3
				сражений за белых (первый игрок) и 3 сражений за черных (второй
				игрок).</p>
		</li>
		<li>
			<h3>По какому принципу проходят сражения на арене?</h3>
			<p>
				Выбираются два бота, последнее сражение которых произошло как можно
				раньше по времени (или, возможно, бот не участвовал в битвах вовсе),
				но при этом они провели менее 6 сражений (см. пред. пункт).
				Результат сражения ботов вносится в <a
					href="${pageContext.request.contextPath}/rating/">рейтинг</a>.
				Журнал сражения можно просмотреть во вкладке <a
					href="${pageContext.request.contextPath}/arena/">арена</a>. Битвы
				проходят одна за одной с максимальной скоростью, которую может
				обеспечить сервер.
			</p>
		</li>
		<li>
			<h3>Можно ли организовать некую тренировочную игру, прежде чем
				выставлять своего бота на соревнования?</h3>
			<p>Для того чтобы узнать как бот играет в игру, достаточно
				отправить его на арену. Он сразу начнёт соревноваться с другими
				ботами и вы сможете наблюдать за его прогрессом через рейтинг. Вы
				всегда можете отправить новую версию своего бота (старая версия бота
				при этом будет снята с арены).</p>
		</li>
		<li>
			<h3>Как подсчитывается рейтинг бота?</h3>
			<p>Рейтинг бота зависит от набранных ботом очков. Очки
				зачисляются по правилу: 3 очка за победу, 1 - за ничью, 0 - за
				поражение.</p>
		</li>
		<li>
			<h3>Могу я просмотреть код моего бота, сражающегося на арене?</h3>
			<p>
				Да, двойным щелчком на строке своего бота в <a
					href="${pageContext.request.contextPath}/rating/">рейтинге</a>.
			</p>
		</li>
		<li>
			<h3>Могу ли я получить пример кода для бота?</h3>
			<p>
				Пожалуйста:<br /> <br />
			</p> 
<h5>Pascal</h5>
<pre> 
var dx,dy,phase,l,n,i,j,k,xn,yn,x,y,d:integer;
    color,xc,yc,c:char;
    field:array[0..10,0..4]of string;
    found:boolean;
begin
  readln(phase);
  readln(color);
  randomize;
  readln(n);
  found:=false;
  for i:=0 to 10 do
      for j:=0 to 4 do
          field[i,j]:='';
  for i:=1 to n do begin
      read(xc);
      read(yc);
      xn := ord(xc)-ord('a');
      yn := ord(yc)-ord('1');
      read(c);
      readln(field[xn,yn]);
  end;
  if phase=1 then begin
      repeat
        y := random(5);
        x := random(11-abs(y-2));
      until length(field[x,y])=0;
      write(chr(ord('a')+x));
      write(y+1);
  end else repeat

      y := random(5);
      x := random(11-abs(y-2));
      if (length(field[x,y])>0) and (field[x,y][length(field[x,y])]=color) then begin
	 if (x > 0) and (x < (10 - abs(y - 2))) and (y > 0) and (y < 4) then begin
	    if (y = 1)
		and (length(field[x - 1,y    ])>0)
		and (length(field[x + 1,y    ])>0)
		and (length(field[x    ,y + 1])>0)
		and (length(field[x + 1,y + 1])>0)
		and (length(field[x    ,y - 1])>0)
		and (length(field[x - 1,y - 1])>0) then continue;
	    if (y = 2)
		and (length(field[x - 1,y    ])>0)
		and (length(field[x + 1,y    ])>0)
		and (length(field[x    ,y + 1])>0)
		and (length(field[x - 1,y + 1])>0)
		and (length(field[x    ,y - 1])>0)
		and (length(field[x - 1,y - 1])>0) then continue;
	    if (y = 3)
		and (length(field[x - 1,y    ])>0)
		and (length(field[x + 1,y    ])>0)
		and (length(field[x    ,y + 1])>0)
		and (length(field[x - 1,y + 1])>0)
		and (length(field[x    ,y - 1])>0)
		and (length(field[x + 1,y - 1])>0) then continue;
         end;
         l:= length(field[x,y]);
	 d:= random(6);
	 if (d = 0) and (x - l >= 0) and (length(field[x - l,y])>0) then begin
            write(chr(ord('a')+x));
            write(y+1);
            write(chr(ord('a')+x-l));
            write(y+1);
	    found:=true;
         end else
	 if (d = 1) and (x + l < (11 - abs(y - 2))) and (length(field[x + l,y])>0) then begin
            write(chr(ord('a')+x));
            write(y+1);
            write(chr(ord('a')+x+l));
            write(y+1);
	    found:=true;
         end else begin
            dx:=0;
            dy:=0;
            if d=2 then for i:=1 to l do begin
                          dy:=dy+1;
                          if (y+dy <=2) then dx:=dx+1 else dx:=dx+0;
            end;
            if d=3 then for i:=1 to l do begin
                          dy:=dy-1;
                          if (y+dy >=2) then dx:=dx+1 else dx:=dx+0;
            end;
            if d=4 then for i:=1 to l do begin
                          dy:=dy+1;
                          if (y+dy <=2) then dx:=dx+0 else dx:=dx-1;
            end;
            if d=5 then for i:=1 to l do begin
                          dy:=dy-1;
                          if (y+dy >=2) then dx:=dx+0 else dx:=dx-1;
            end;
	    if (abs(dx) + abs(dy) <> 0) and (x + dx >= 0) and (x + dx < (11 - abs(y + dy - 2)))
	       and (y + dy >= 0) and (y + dy < 5) and (length(field[x + dx][y + dy])>0) then begin
               write(chr(ord('a')+x));
               write(y+1);
               write(chr(ord('a')+x+dx));
               write(y+1+dy);
	       found:=true;
            end
         end;
      end;
  until found;
end.
</pre>
<h5>Java</h5>
<pre>
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class main
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		PrintWriter out = new PrintWriter(System.out);
		int phase = in.nextInt();
		in.nextLine();
		String color = in.nextLine();
		int count = in.nextInt();
		in.nextLine();
		String[][] field = new String[11][5];
		for (int i = 0; i < count; i++)
		{
			String f = in.nextLine();
			int x = Character.getNumericValue(f.charAt(0)) - Character.getNumericValue('a');
			int y = Character.getNumericValue(f.charAt(1)) - Character.getNumericValue('1');
			field[x][y] = f.substring(3);
		}
		Random r = new Random();
		if (phase == 1)
		{
			// если фаза 1 - занимаем любую пустую ячейку
			int y = 0;
			int x = 0;
			do { 
				y = r.nextInt(5);
				x = r.nextInt(11 - Math.abs(y - 2));
			}while (field[x][y] != null);
				out.print((char) (97 + x));
				out.print(1 + y);
		} else
		{
			// если фаза 2 - случайным образом выбираем ячейку своего цвета 
			// и пытаемся её сдвинуть - если получается - двигаем
			boolean found = false;
			do
			{
				int y = r.nextInt(5);
				int x = r.nextInt(11 - Math.abs(y - 2));
				if (field[x][y] != null && !field[x][y].equals("")
						&& field[x][y].charAt(field[x][y].length() - 1) == color.charAt(0))
				{
					// Проверить что выбранная клетка не "окружена"
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

					int l = field[x][y].length();
					int d = r.nextInt(6);
					//ход влево
					if (d == 0 && x - l >= 0 && field[x - l][y] != null && !field[x - l][y].equals(""))
					{
						out.print((char) (97 + x));
						out.print(1 + y);
						out.print((char) (97 + x - l));
						out.print(1 + y);
						found = true;
					} else
					//ход вправо
					if (d == 1 && x + l < (11 - Math.abs(y - 2)) && field[x + l][y] != null && !field[x + l][y].equals(""))
					{
						out.print((char) (97 + x));
						out.print(1 + y);
						out.print((char) (97 + x + l));
						out.print(1 + y);
						found = true;
					} else
					{
						//ход ввверх-вправо
						int dx = 0;
						int dy = 0;
						if (d == 2)
						{
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
						}
						//ход вниз-вправо
						if (d == 3)
						{
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
						}
						//ход ввверх-влево
						if (d == 4)
						{
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
						}
						//ход вниз-влево
						if (d == 5)
						{
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
						}

						if (Math.abs(dx) + Math.abs(dy) != 0 && x + dx >= 0 && x + dx < (11 - Math.abs(y + dy - 2))
								&& y + dy >= 0 && y + dy < 5 && field[x + dx][y + dy] != null
								&& !field[x + dx][y + dy].equals(""))
						{
							out.print((char) (97 + x));
							out.print(1 + y);
							out.print((char) (97 + x + dx));
							out.print(1 + y + dy);
							found = true;
						}
					}
				}
			} while (!found);
		}
		out.flush();
	}
}
</pre>
<h5>C++</h5>
<pre>
#include &lt;iostream&gt;
using namespace std;

int main() {
 int phase;
 cin >> phase; 
 if (phase==1) {
   cout << "a1";
 } else {
   cout << "a1b1";
 }
 return 0;
} 
</pre>
<h5>Python</h5>
<pre>
#! python

phase = int(raw_input())

if phase ==1:
      print 'a1'
else:
      print 'a1b1'

</pre>
		</li>
	</ol>
</div>
<%@ include file="/WEB-INF/pages/footer.jsp"%>