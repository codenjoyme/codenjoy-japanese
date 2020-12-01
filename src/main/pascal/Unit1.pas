unit Unit1;
unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ExtCtrls, StdCtrls, Spin;

const WH = 15;
      LengthArr = 255;

type
  TDotsArr = array [1..LengthArr, 1..LengthArr] of boolean;
  TLeft    = array [1..10, 1..LengthArr] of byte;
  TTop     = array [1..LengthArr, 1..10] of byte;
  TForm1 = class(TForm)
    pb: TPaintBox;
    seSizeX: TSpinEdit;
    seSizeY: TSpinEdit;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure pbPaint(Sender: TObject);
    procedure pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure seSizeYChange(Sender: TObject);
  private
    Dots:TDotsArr;
    LeftCifer:TLeft;
    TopCifer:TTop;
    Buf, LeftBmp, TopBmp, CenterBmp:TBitMap;
    LeftCount, TopCount:integer;
    Size:TPoint;
  public
    procedure Main;
    procedure DotFromCoord(var x, y:integer);
    procedure DrawTextCenter(Canvas: TCanvas; Rect: TRect; str:string);
    procedure ClearDotsArr;
    procedure ClearTopArr;
    procedure ClearLeftArr;
    procedure CalculateLeftTop;
    procedure DrawLeft;
    procedure DrawTop;
    procedure DrawAll;
    procedure Build;
    procedure DrawCenter;
  end;

var
  Form1: TForm1;

implementation

{$R *.DFM}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.Build;
begin
    ClearDotsArr;
    Dots[1, 1]:=false; Dots[2, 1]:=false; Dots[3, 1]:=false; Dots[4, 1]:=false; Dots[5, 1]:=true;  Dots[6, 1]:=true;  Dots[7, 1]:=true;  Dots[8, 1]:=true;  Dots[9, 1]:=true;  Dots[10, 1]:=true;
    Dots[1, 2]:=false; Dots[2, 2]:=false; Dots[3, 2]:=false; Dots[4, 2]:=true;  Dots[5, 2]:=true;  Dots[6, 2]:=false; Dots[7, 2]:=true;  Dots[8, 2]:=false; Dots[9, 2]:=false; Dots[10, 2]:=true;
    Dots[1, 3]:=false; Dots[2, 3]:=false; Dots[3, 3]:=false; Dots[4, 3]:=true;  Dots[5, 3]:=false; Dots[6, 3]:=false; Dots[7, 3]:=true;  Dots[8, 3]:=false; Dots[9, 3]:=false; Dots[10, 3]:=true;
    Dots[1, 4]:=false; Dots[2, 4]:=true;  Dots[3, 4]:=true;  Dots[4, 4]:=true;  Dots[5, 4]:=true;  Dots[6, 4]:=true;  Dots[7, 4]:=true;  Dots[8, 4]:=true;  Dots[9, 4]:=true;  Dots[10, 4]:=true;
    Dots[1, 5]:=false; Dots[2, 5]:=true;  Dots[3, 5]:=true;  Dots[4, 5]:=true;  Dots[5, 5]:=true;  Dots[6, 5]:=true;  Dots[7, 5]:=true;  Dots[8, 5]:=true;  Dots[9, 5]:=true;  Dots[10, 5]:=true;
    Dots[1, 6]:=true;  Dots[2, 6]:=true;  Dots[3, 6]:=true;  Dots[4, 6]:=true;  Dots[5, 6]:=true;  Dots[6, 6]:=true;  Dots[7, 6]:=true;  Dots[8, 6]:=true;  Dots[9, 6]:=true;  Dots[10, 6]:=true;
    Dots[1, 7]:=false; Dots[2, 7]:=false; Dots[3, 7]:=true;  Dots[4, 7]:=true;  Dots[5, 7]:=false; Dots[6, 7]:=false; Dots[7, 7]:=false; Dots[8, 7]:=true;  Dots[9, 7]:=true;  Dots[10, 7]:=false;
    Dots[1, 8]:=false; Dots[2, 8]:=false; Dots[3, 8]:=true;  Dots[4, 8]:=true;  Dots[5, 8]:=false; Dots[6, 8]:=false; Dots[7, 8]:=false; Dots[8, 8]:=true;  Dots[9, 8]:=true;  Dots[10, 8]:=false;
    Dots[1, 9]:=false; Dots[2, 9]:=false; Dots[3, 9]:=false; Dots[4, 9]:=false; Dots[5, 9]:=false; Dots[6, 9]:=false; Dots[7, 9]:=false; Dots[8, 9]:=false; Dots[9, 9]:=false; Dots[10, 9]:=false;
    Dots[1,10]:=false; Dots[2,10]:=false; Dots[3,10]:=false; Dots[4,10]:=false; Dots[5,10]:=false; Dots[6,10]:=false; Dots[7,10]:=false; Dots[8,10]:=false; Dots[9,10]:=false; Dots[10,10]:=false;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.CalculateLeftTop;
var x, y:integer;
    LeftMax, TopMax, m:integer;
    b1, b2, b3:boolean;
    i:integer;
    c:integer;
    tArr:array [1..10] of integer;
begin
    ClearLeftArr;
    ClearTopArr;
    {боковые числа}
    LeftMax:=0;
    for y:=1 to Size.y do begin
        b1:=false;
        c:=0;
        b3:=false;
        m:=0;
        x:=Size.x;
        while (x <> 0) do begin
            b2:=Dots[x, y];
            if (b3)
                then begin
                    if (b2 xor b1)
                        then begin  // счет закончился
                            tArr[m]:=c;
                            c:=0;
                            b3:=false;
                        end
                        else inc(c); // счет идет
                end
                else begin
                    b3:=(b2 xor b1);
                    if (b3) then begin // счет начался
                        inc(c);
                        inc(m);
                        if (m > LeftMax) then LeftMax:=m;
                    end
                end;
                b1:=b2;
                dec(x);
                if ((x = 0) and b1) then tArr[m]:=c;
        end;
        for i:=1 to m do LeftCifer[i, y]:=tArr[i];
    end;
    LeftCount:=LeftMax;
    for y:=1 to Size.y do
        for x:=1 to (LeftCount div 2) do begin
            c:=LeftCifer[x, y];
            LeftCifer[x, y]:=LeftCifer[LeftCount - x + 1, y];
            LeftCifer[LeftCount - x + 1, y]:=c;
        end;
    {верхние числа}
    TopMax:=0;
    for x:=1 to Size.x do begin
        b1:=false;
        c:=0;
        b3:=false;
        m:=0;
        y:=Size.y;
        while (y <> 0) do begin
            b2:=Dots[x, y];
            if (b3)
                then begin
                    if (b2 xor b1)
                        then begin  // счет закончился
                            tArr[m]:=c;
                            c:=0;
                            b3:=false;
                        end
                        else inc(c); // счет идет
                end
                else begin
                    b3:=(b2 xor b1);
                    if (b3) then begin // счет начался
                        inc(c);
                        inc(m);
                        if (m > TopMax) then TopMax:=m;
                    end
                end;
                b1:=b2;
                dec(y);
                if ((y = 0) and b1) then tArr[m]:=c;
        end;
        for i:=1 to m do TopCifer[x, i]:=tArr[i];
    end;
    TopCount:=TopMax;
    for x:=1 to Size.x do
        for y:=1 to (TopCount div 2) do begin
            c:=TopCifer[x, y];
            TopCifer[x, y]:=TopCifer[x, TopCount - y + 1];
            TopCifer[x, TopCount - y + 1]:=c;
        end;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ClearDotsArr;
var x, y:integer;
begin
    for y:=1 to LengthArr do
        for x:=1 to LengthArr do
            Dots[x, y]:=false;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ClearLeftArr;
var x, y:integer;
begin
    LeftCount:=0;
    for y:=1 to LengthArr do
        for x:=1 to 10 do
            LeftCifer[x, y]:=0;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ClearTopArr;
var x, y:integer;
begin
    TopCount:=0;
    for y:=1 to 10 do
        for x:=1 to LengthArr do
            TopCifer[x, y]:=0;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawCenter;
var x, y:integer;
    rct:TRect;
begin
    CenterBmp.Width:=Size.x*WH;
    CenterBmp.Height:=Size.y*WH;
    for x:=0 to Size.x do
        for y:=0 to Size.y do begin
           rct:=Rect(x*WH, y*WH, (x+1)*WH, (y+1)*WH);
            if (Dots[x + 1, y + 1])
                then begin
                    CenterBmp.Canvas.Brush.Color:=clBlack;
                    CenterBmp.Canvas.Rectangle(rct);
                end
                else begin
                    CenterBmp.Canvas.Brush.Color:=clWhite;
                    CenterBmp.Canvas.Rectangle(rct);
                    CenterBmp.Canvas.MoveTo(rct.Left, rct.Top);
                    CenterBmp.Canvas.LineTo(rct.Right, rct.Bottom);
                    CenterBmp.Canvas.MoveTo(rct.Right, rct.Top);
                    CenterBmp.Canvas.LineTo(rct.Left, rct.Bottom);
                end;
        end;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawLeft;
var x, y:integer;
    rct:TRect;
begin
    LeftBmp.Width:=LeftCount*WH;
    LeftBmp.Height:=Size.y*WH;
    for x:=0 to (LeftCount - 1) do
        for y:=0 to (Size.y - 1) do begin
            rct:=Rect(x*WH, y*WH, (x+1)*WH, (y+1)*WH);
            LeftBmp.Canvas.Rectangle(rct);
            if (LeftCifer[x + 1, y + 1] <> 0) then DrawTextCenter(LeftBmp.Canvas, rct, IntToStr(LeftCifer[x + 1, y + 1]));
        end;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawTop;
var x, y:integer;
    rct:TRect;
begin
    TopBmp.Width:=Size.x*WH;
    TopBmp.Height:=TopCount*WH;
    for x:=0 to (Size.x - 1) do
        for y:=0 to (TopCount - 1) do begin
            rct:=Rect(x*WH, y*WH, (x+1)*WH, (y+1)*WH);
            TopBmp.Canvas.Rectangle(rct);
            if (TopCifer[x + 1, y + 1] <> 0) then DrawTextCenter(TopBmp.Canvas, rct, IntToStr(TopCifer[x + 1, y + 1]));
        end;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawAll;
begin
    Buf.Width:=(LeftCount + Size.x)*WH;
    Buf.Height:=(TopCount + Size.y)*WH;
    Buf.Canvas.Rectangle(0, 0, LeftCount*WH, TopCount*WH);
    Buf.Canvas.Draw(0, TopCount*WH, LeftBmp);
    Buf.Canvas.Draw(LeftCount*WH, 0, TopBmp);
    Buf.Canvas.Draw(LeftCount*WH, TopCount*WH, CenterBmp);
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormCreate(Sender: TObject);
begin
    Buf:=TBitMap.Create;
    Buf.PixelFormat:=pf24bit;

    LeftBmp:=TBitMap.Create;
    LeftBmp.PixelFormat:=pf24bit;

    TopBmp:=TBitMap.Create;
    TopBmp.PixelFormat:=pf24bit;

    CenterBmp:=TBitMap.Create;
    CenterBmp.PixelFormat:=pf24bit;

    LeftCount:=0;
    TopCount:=0;
    Size:=Point(10, 10);
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormDestroy(Sender: TObject);
begin
    Buf.FreeImage;
    LeftBmp.FreeImage;
    TopBmp.FreeImage;
    CenterBmp.FreeImage;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------.
procedure TForm1.Main;
begin
    CalculateLeftTop;
    DrawLeft;
    DrawTop;
    DrawCenter;
    DrawAll;
    pb.Width:=Buf.Width;
    pb.Height:=Buf.Height;        
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawTextCenter(Canvas: TCanvas; Rect: TRect; str:string);
var iLeft, iTop:integer;
begin
    iLeft:=Rect.Left + (Rect.Right - Rect.Left - Canvas.TextWidth(str)) div 2;
    iTop:=Rect.Top + (Rect.Bottom - Rect.Top - Canvas.TextHeight(str)) div 2;;
    Canvas.TextOut(iLeft, iTop, str);
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbPaint(Sender: TObject);
begin
    pb.Canvas.Draw(0, 0, Buf);
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
begin
    DotFromCoord(X, Y);
    if (X < 1) or (Y < 1) or (X > Size.x) or (Y > Size.y) then Exit;  
    Dots[X, Y]:=not Dots[X, Y];
    Main;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DotFromCoord(var x, y: integer);
begin
    x:=((x - LeftCount*WH) div WH) + 1;
    y:=((y - TopCount*WH) div WH) + 1;    
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.seSizeYChange(Sender: TObject);
begin
    Size.x:=seSizeX.Value;
    Size.y:=seSizeY.Value;
    ClearDotsArr;
    Main;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
