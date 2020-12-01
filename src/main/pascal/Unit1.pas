unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls, ExtCtrls;

type
  TData = record
     Pos, Ass:TPoint;
     Check:byte; // 0 - пусто 1 - чек 2 - нечек
  end;
  TRjad = array [1..40] of byte;
  TBitArray = array [1..40] of boolean;
  PBitArray = ^TBitArray;
  TCombine = array [1..1000] of TBitArray;
  TForm1 = class(TForm)
    edCount: TEdit;
    udCount: TUpDown;
    pb: TPaintBox;
    btCalc: TButton;
    Label1: TLabel;
    Button1: TButton;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure pbPaint(Sender: TObject);
    procedure edCountChange(Sender: TObject);
    procedure pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
    procedure pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure btCalcClick(Sender: TObject);
    procedure Button1Click(Sender: TObject);
  private
    PredCoord:TPoint; PredButt:TShiftState;
    bDown:boolean;
    Buf, bmpLeft, bmpPole:TBitMap;
    Data:array [1..40] of TData;

    Combine:TCombine;
    CountCombine:integer;
    NumCombine:integer;

    Rjad:TRjad;
    CountRjad:integer;
    procedure Draw;
    procedure DrawPole;
    procedure DrawLeft;
    procedure GetRjad;
    procedure ClearData;
    procedure GetCombine;
    procedure GetNextCombine;
    procedure GetRjadFromCombine(var r: TRjad; var cr:integer; bits:PBitArray); // тут р¤д включает количества чередующихс¤ пустых и непустых! ¤чеек
    procedure GetCombineFromRjad(var r: TRjad; var cr:integer; bits:PBitArray); // тут р¤д включает количества чередующихс¤ пустых и непустых! ¤чеек
    procedure ManipuleRjad(var r: TRjad; var cr:integer);
  public
    { Public declarations }
  end;

var Form1: TForm1;
const wid = 15;
implementation

{$R *.DFM}
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.Draw;
begin
    DrawPole;
    DrawLeft;
    Buf.Width:=bmpLeft.Width + bmpPole.Width;
    Buf.Height:=bmpLeft.Height;
    Buf.Canvas.Draw(0, 0, bmpLeft);
    Buf.Canvas.Draw(bmpLeft.Width, 0, bmpPole);
    pb.Repaint;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawLeft;
var i, x, y:integer;
    tstr:string;
begin
    bmpLeft.Width:={CountRjad}((udCount.Position+1) div 2)*wid;
    bmpLeft.Height:=wid;
    bmpLeft.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpLeft.Height);
    for i:=1 to CountRjad do begin
        x:=(i - 1)*wid; y:=0;
        bmpLeft.Canvas.Rectangle(x, y, i*wid, wid);
        tstr:=IntToStr(Rjad[i]);
        bmpLeft.Canvas.TextOut(x + (wid - bmpLeft.Canvas.TextWidth(tstr) + 1) div 2,
                               y + (wid - bmpLeft.Canvas.TextHeight(tstr)) div 2, tstr);
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawPole;
var i:integer;
begin
    bmpPole.Width:=udCount.Position*wid;
    bmpPole.Height:=wid;
    bmpPole.Canvas.Rectangle(0, 0, bmpPole.Width, bmpPole.Height);
    for i:=1 to udCount.Position do begin
        case (Data[i].Check) of
            0: bmpPole.Canvas.Brush.Color:=clWhite;
            1: bmpPole.Canvas.Brush.Color:=clLtGray;
            2: bmpPole.Canvas.Brush.Color:=clDkGray;
        end;
        bmpPole.Canvas.Rectangle((i - 1)*wid, 0, i*wid, wid);
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormCreate(Sender: TObject);
begin
    PredCoord:=Point(-1, -1);
    PredButt:=[ssLeft];
    bDown:=false;

    Buf:=TBitMap.Create;
    Buf.PixelFormat:=pf24bit;
    Buf.Width:=10;
    Buf.Height:=10;
    bmpLeft:=TBitMap.Create;
    bmpLeft.PixelFormat:=pf24bit;
    bmpLeft.Width:=10;
    bmpLeft.Height:=10;
    bmpLeft.Canvas.Pen.Color:=clBlack;
    bmpLeft.Canvas.Brush.Color:=clWhite;
    bmpLeft.Canvas.Font.Name:='Times New Roman';
    bmpLeft.Canvas.Font.Size:=7;

    bmpPole:=TBitMap.Create;
    bmpPole.PixelFormat:=pf24bit;
    bmpPole.Width:=10;
    bmpPole.Height:=10;
    bmpPole.Canvas.Pen.Color:=clBlack;
    bmpPole.Canvas.Brush.Color:=clWhite;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormDestroy(Sender: TObject);
begin
    Buf.Free;
    bmpLeft.Free;
    bmpPole.Free;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjad;
var i, a:integer;
begin
    a:=0; CountRjad:=1;
    for i:=1 to udCount.Position do begin
        if (Data[i].Check = 1)
            then inc(a)
            else
                if (a <> 0) then begin
                    Rjad[CountRjad]:=a;
                    a:=0;
                    inc(CountRjad);
                end;
    end;
    if (a <> 0) then begin
        Rjad[CountRjad]:=a;
        inc(CountRjad);
    end;
    dec(CountRjad);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
begin
    bDown:=true;
    pbMouseMove(Sender, Shift, X, Y);    
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbPaint(Sender: TObject);
begin
    pb.Height:=Buf.Height;
    pb.Width:=Buf.Width;
    pb.Height:=Buf.Height;
    pb.Canvas.Draw(0, 0, Buf);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.edCountChange(Sender: TObject);
begin
    ClearData;
    GetRjad;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ClearData;
var i:integer;
begin
    for i:=1 to udCount.Position do begin
        Data[i].Pos:=Point(i, 0);
        Data[i].Ass:=Point(0, 0);
        Data[i].Check:=0;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
var bDraw:boolean;
begin
    if (not bDown) then Exit;
    X:=X - bmpLeft.Width;
    X:=(X div wid) + 1;
    if ((X <= 0) or (X > udCount.Position)) then Exit;
    if ((PredButt = Shift) and (PredCoord.x = X)) then Exit;
    bDraw:=false;
    if (ssLeft in Shift)
        then begin
            bDraw:=(Data[X].Check <> 1);
            Data[X].Check:=1
        end
        else begin
            bDraw:=(Data[X].Check <> 0);
            Data[X].Check:=0;
        end;
    PredCoord:=Point(x, 1);
    PredButt:=Shift;
    if (bDraw) then begin
        GetRjad;
        Draw;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
begin
    bDown:=false;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btCalcClick(Sender: TObject);
var i, j:integer;
    b:boolean;
begin
    for i:=1 to udCount.Position do Data[i].Check:=0;
    GetCombine;
    for i:=1 to udCount.Position do begin
        b:=true;
        for j:=1 to CountCombine do begin
            b:=b and Combine[j, i];
        end;
        if (b) then Data[i].Check:=1;
    end;
    GetRjad;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.Button1Click(Sender: TObject);
var i, j:integer;
    b:boolean;
begin
    for i:=1 to udCount.Position do Data[i].Check:=0;
    GetNextCombine;
    for i:=1 to udCount.Position do begin
        b:=true;
{df}        for j:=CountCombine to CountCombine do begin
            b:=b and Combine[j, i];
        end;
        if (b) then Data[i].Check:=1;
    end;
    GetRjad;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetCombine;
var i, j, x:integer;
begin
    CountCombine:=1;
    x:=1;
    for i:=1 to CountRjad do begin
        for j:=x to (x + Rjad[i] - 1) do begin
            Combine[CountCombine, j]:=true;
        end;
        x:=x + Rjad[i];
        Combine[CountCombine, x]:= false;
        x:=x + 1;
    end;
    for i:=x to udCount.Position do begin
        Combine[CountCombine, i]:= false;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetNextCombine;
var i:integer;
    bits1, bits2:PBitArray;

    Rjad10:TRjad;
    cr:integer;
begin
    inc(CountCombine);
    inc(NumCombine);
    bits1:=@Combine[CountCombine - 1];
    bits2:=@Combine[CountCombine];

    GetRjadFromCombine(Rjad10, cr, bits1);
    ManipuleRjad(Rjad10, cr);
    GetCombineFromRjad(Rjad10, cr, bits2);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetCombineFromRjad(var r: TRjad; var cr:integer; bits:PBitArray);
var b:boolean;
    i, j, x:integer;
begin
    b:=true; x:=0;
    for i:=1 to cr do begin
        for j:=1 to r[i] do
            bits^[x + j]:=b;
        x:=x + r[i];
        b:=not b;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjadFromCombine(var r: TRjad; var cr: integer; bits: PBitArray);
var i, j:integer;
    b:boolean;
begin
    b:=bits^[1];
    j:=1; cr:=1;
    for i:=2 to udCount.Position do begin
        if (bits[i] xor b) then begin
            r[cr]:=j;
            inc(cr);
            j:=0;
        end;
        inc(j);
        b:=bits^[i];
    end;
    if (j <> 0) then r[cr]:=j;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ManipuleRjad(var r: TRjad; var cr: integer);
begin
    
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
