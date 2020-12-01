unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ExtCtrls;

const WH = 5;
      LengthArr = 255;

type
  TDotsArr = array [1..LengthArr, 1..LengthArr] of boolean;
  TLeft    = array [1..10, 1..LengthArr] of byte;
  TTop     = array [1..LengthArr, 1..10] of byte;
  TForm1 = class(TForm)
    pb: TPaintBox;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure pbClick(Sender: TObject);
  private
    Dots:TDotsArr;
    LeftCifer:TLeft;
    TopCifer:TTop;
    Buf, LeftBmp, TopBmp, CenterBmp:TBitMap;
    LeftCount, TopCount:integer;
    Size:TPoint;    
  public
    procedure ClearDotsArr;
    procedure ClearTopArr;
    procedure ClearLeftArr;
    procedure CalculateLeftTop;
    procedure Build;
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
    LeftMax, TopMax, lm, tm:integer;
    b1, b2:boolean;
    i, j:integer;
    tArr:array [1..10] of integer;
begin
    ClearLeftArr;
    ClearTopArr;
    LeftMax:=0;
    TopMax:=0;
    for y:=1 to Size.y do begin
        x:=Size.x;
        b1:=Dots[x, y];
        b2:=false;
        lm:=0;
        i:=0;
        if (b1) then begin
            i:=1;
            lm:=1;
            LeftMax:=1;
        end;
        for x:=(Size.x - 1) downto 1 do begin
            if (Dots[x, y]) then Inc(i);
            if ((Dots[x, y] <> b1) and b2) then begin
                tArr[lm]:=i;
                inc(lm);
                if (lm > LeftMax) then LeftMax:=lm;
            end;
            if ((Dots[x, y] <> b1) and (not Dots[x, y]))
                then b2:=true; //
            b1:=Dots[x, y];
        end;
    end;
    LeftCount:=LeftMax;
    LeftCount:=i;
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
procedure TForm1.FormCreate(Sender: TObject);
begin
    Buf:=TBitMap.Create;
    Buf.Width:=pb.Width;
    Buf.Height:=pb.Height;
    Buf.PixelFormat:=pf24bit;

    LeftBmp:=TBitMap.Create;
    LeftBmp.Width:=pb.Width;
    LeftBmp.Height:=pb.Height;
    LeftBmp.PixelFormat:=pf24bit;

    TopBmp:=TBitMap.Create;
    TopBmp.Width:=pb.Width;
    TopBmp.Height:=pb.Height;
    TopBmp.PixelFormat:=pf24bit;

    CenterBmp:=TBitMap.Create;
    CenterBmp.Width:=pb.Width;
    CenterBmp.Height:=pb.Height;
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
procedure TForm1.pbClick(Sender: TObject);
begin
    Build;
    CalculateLeftTop;
end;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.