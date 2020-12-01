unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls, ExtCtrls;

type
  TData = record
     Pos, Ass:TPoint;
     Check:byte; // 0 - ����� 1 - ��� 2 - �����
  end;
  TRjad10 = array [1..40] of record
    c:byte;
    b:boolean;
  end;
  TRjad = array [1..40] of byte;
  TBitArray = array [1..40] of boolean;
  PBitArray = ^TBitArray;
  TCombine = array [1..100000] of TBitArray;
  TForm1 = class(TForm)
    edCount: TEdit;
    udCount: TUpDown;
    pb: TPaintBox;
    btCalc: TButton;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure pbPaint(Sender: TObject);
    procedure edCountChange(Sender: TObject);
    procedure pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
    procedure pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure btCalcClick(Sender: TObject);
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
    procedure GetRjadFromCombine(var r: TRjad10; var cr:integer; bits:PBitArray); // ��� ��� �������� ���������� ������������ ������ � ��������! �����
    procedure GetCombineFromRjad(var r: TRjad10; var cr:integer; bits:PBitArray); // ��� ��� �������� ���������� ������������ ������ � ��������! �����
    function  ManipuleRjad(var r: TRjad10; var cr:integer):boolean;
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
            1: bmpPole.Canvas.Brush.Color:=clBlack;
            2: bmpPole.Canvas.Brush.Color:=clLtGray;
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
var i, j, k:integer;
    b:boolean;
begin
    if (X < bmpLeft.Width) then begin
        X:=(X div wid) + 1;
        if (ssLeft in Shift) then begin
            j:=0;
            for i:=1 to CountRjad do
                j:=j + Rjad[i];
            if (X > CountRjad) then begin
                X:=CountRjad + 1;
                if ((j + CountRjad) > udCount.Position - 1) then Exit;
                inc(CountRjad);
                Rjad[X]:=0;
            end;
            if ((j + CountRjad) > udCount.Position) then Exit;
            Rjad[X]:=Rjad[X] + 1;
        end;
        if (ssRight in Shift) then begin
            if (X > CountRjad) then X:=CountRjad;
            Rjad[X]:=Rjad[X] - 1;
            if (Rjad[X] = 0) then begin
                if (X < CountRjad) then
                    for i:=X to CountRjad do
                        Rjad[i]:=Rjad[i + 1];
                dec(CountRjad);
            end;
        end;

        for i:=1 to udCount.Position do Data[i].Check:=0;
        k:=1; i:=1;
        while (i <= CountRjad) do begin
            for j:=1 to Rjad[i] do
                Data[k + j - 1].Check:=1;
            Data[k + j].Check:=0;
            k:=k + j;
            inc(i);                                    
        end;

        Draw;
        Exit;
    end;
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
    if (ssLeft in Shift) then begin
        bDraw:=(Data[X].Check <> 1);
        Data[X].Check:=1
    end;
    if (ssRight in Shift) then begin
        bDraw:=(Data[X].Check <> 0);
        Data[X].Check:=0;
    end;
    if (ssMiddle in Shift) then begin
        bDraw:=(Data[X].Check <> 2);
        Data[X].Check:=2;
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
    b, b2:boolean;
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

    for i:=1 to udCount.Position do Data[i].Check:=0;
    GetNextCombine;
    for i:=1 to udCount.Position do begin
        b:=true;
        b2:=false;
{df}    for j:=1 to CountCombine do begin
            b:=b and Combine[j, i];
            b2:=b2 or Combine[j, i];
        end;
        if (b) then Data[i].Check:=1;
        if (not b2) then Data[i].Check:=2;
    end;
    GetRjad;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetCombine;
var i, j, x:integer;
begin
    CountCombine:=1;
    NumCombine:=1;
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
    b:boolean;
    Rjad10:TRjad10;
    cr:integer;
begin
    b:=true;
    while (b) do begin
        inc(CountCombine);
        inc(NumCombine);
        bits1:=@Combine[CountCombine - 1];
        bits2:=@Combine[CountCombine];

        GetRjadFromCombine(Rjad10, cr, bits1);
        b:=ManipuleRjad(Rjad10, cr);
        GetCombineFromRjad(Rjad10, cr, bits2);
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetCombineFromRjad(var r: TRjad10; var cr:integer; bits:PBitArray);
var  i, j, x:integer;
begin
    x:=0;
    for i:=1 to cr do begin
        for j:=1 to r[i].c do
            bits^[x + j]:=r[i].b;
        x:=x + r[i].c;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjadFromCombine(var r: TRjad10; var cr: integer; bits: PBitArray);
var i, j:integer;
    b:boolean;
begin
    b:=bits^[1];
    j:=1; cr:=1;
    for i:=2 to udCount.Position do begin
        if (bits[i] xor b) then begin
            r[cr].c:=j;
            r[cr].b:=b;
            inc(cr);
            j:=0;
        end;
        inc(j);
        b:=bits^[i];
    end;
    if (j <> 0) then begin
        r[cr].c:=j;
        r[cr].b:=b;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TForm1.ManipuleRjad(var r: TRjad10; var cr: integer):boolean;
var i, a, a2:integer;
    b, b2:boolean;
begin
    a:=cr;
    Result:=true;
    while (true) do begin
   {|}  if (r[a].b)
   {|}      then begin
   {|}          a:=a - 1;
   {|}          if (a <= 0) then begin
   {|}              Result:=false;
   {|--------------}Break;
   {|}          end;
   {|}          b:=true;
   {|}          while true do begin
   {|}         {|}  a2:=2;
   {|}         {|}  if (not b)
   {|}         {|------}then Continue;
   {|}         {|}  b:=not b;
   {|}         {|}
   {|}         {|}  if ((a + 2) > cr) then begin
   {|}         {X------}if ((a + 2) <> (cr + 1)) then break;
   {|}         {X------}if (not r[cr].b) then break;
   {|}         {|} {|}
   {|}         {|} {|}  b2:=false;
   {|}         {|} {|}  if ((r[a].c <> 1) and (cr = 2)) then b2:=true;
   {|}         {|} {|}  while (r[a].c = 1) do begin
   {|}         {|} {|} {|}  a:=a - 2;
   {|}         {|} {|} {|}  a2:=a2 + 2;
   {|}         {|} {|} {|}  if ((a <= 0) or (a2 = cr)) then begin
   {|}         {|} {|} {|}      b2:=true;
   {|}         {|} {|} {X-------}break;
   {|}         {|} {|} {|}  end;
   {|}         {|} {|}  end;
   {|}         {X------}if (b2) then Break;
   {|}         {|} {|}
   {|}         {|} {|}  inc(cr);
   {|}         {|} {|}  r[cr].b:=false;
   {|}         {|} {|}  r[cr].c:=0;
   {|}         {|}  end;
   {|}         {|}  r[a + a2].c:=r[a + a2].c + r[a].c - 2;
   {|}         {|}  r[a].c:=2;
   {|}         {X--}Break;
   {|}          end;
   {|}          if (b2) then begin
   {|}         {|}  Result:=false;
   {|--------------}Break;
   {|}          end;
   {|}      end
   {|}      else begin
   {|}     {|}  if (cr = 1) then begin
   {|}     {|} {|}  Result:=false;
   {X--------------}Break;
   {|}     {|}  end;
   {|}     {|}  if ((a - 2) <= 0) then begin
   {X--------------}if (a < 1) then Break;
   {|}     {|} {|}  inc(cr);
   {|}     {|} {|}  for i:=(cr - 1) downto 1 do begin
   {|}     {|} {|} {|}  r[i + 1].c:=r[i].c;
   {|}     {|} {|} {|}  r[i + 1].b:=r[i].b;
   {|}     {|} {|}  end;
   {|}     {|} {|}  r[1].c:=1;
   {|}     {|} {|}  r[1].b:=false;
   {|}     {|} {|}  if (cr > 3) then r[3].c:=1;
   {|}     {|} {|}  if (cr = 3) then r[3].c:=r[3].c - 1;
   {X--------------}Break;
   {|}     {|}  end;
   {|}     {|}  r[a - 2].c:=r[a - 2].c + 1;
   {|}     {|}  r[a].c:=r[a].c - 1;
   {|}     {|}  if (r[a].c = 0)
   {|}     {|}      then begin
   {|}     {|}     {|}  if (a = cr)
   {|}     {|}     {|}      then begin
   {|}     {|}     {|}     {|}  dec(cr);
   {X---------------------------}Break;
   {|}     {|}     {|}      end
   {|}     {|}     {|}      else begin
   {|}     {|}     {|}     {|}  r[a - 2].c:=r[a - 2].c - 1;
   {|}     {|}     {|}     {|}  r[a].c:=r[a].c + 1;
   {|}     {|}     {|}     {|}  a:=a - 2;
   {|}     {|}     {|}      end;
   {|---------------------}continue;
   {|}     {|}      end
   {|}     {|}      else begin
//   {|}     {|}     {|}   dec(cr);
   {X---------------------}Break; 
   {|}     {|}      end;
   {|}      end;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
