unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls, ExtCtrls;

type
  TDataPt = record
     Pos, Ass:TPoint;
     Check:byte; // 0 - пусто 1 - чек 2 - нечек
  end;
  TData = array [1..40] of TDataPt;  
  TRjad10 = array [1..40] of record
    c:byte;
    b:boolean;
  end;
  TRjad = array [1..40] of byte;
  TBitArray = array [1..40] of boolean;
  TComb = array [1..100000] of TBitArray;
  TForm1 = class(TForm)
    edCount: TEdit;
    udCount: TUpDown;
    pb: TPaintBox;
    btCalc: TButton;
    cbMode: TCheckBox;
    edCountY: TEdit;
    udCountY: TUpDown;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure pbPaint(Sender: TObject);
    procedure edCountChange(Sender: TObject);
    procedure pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
    procedure pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure btCalcClick(Sender: TObject);
    procedure cbModeClick(Sender: TObject);
  private
    PredCoord:TPoint; PredButt:TShiftState;
    bDown:boolean;
    Buf, bmpLeft, bmpPole:TBitMap;

    Data:TData;

    Comb:TComb;
    CountComb:integer;
    Len:integer;

    Rjad:TRjad;
    CountRjad:integer;
    procedure Draw;
    procedure DrawPole;
    procedure DrawLeft;
    procedure GetRjad;
    procedure ClearData;
    procedure GetRjadFromComb(var r: TRjad10; var cr:integer; var bits:TBitArray); // тут р€д включает количества чередующихс€ пустых и непустых! €чеек
    procedure GetCombFromRjad(var r: TRjad10; var cr:integer; var bits:TBitArray); // тут р€д включает количества чередующихс€ пустых и непустых! €чеек
    function  ManipuleRjad(var r: TRjad10; var cr:integer):boolean;
    function  TestComb(var dt:TData; l:integer; var bits:TBitArray):boolean;
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
    bmpLeft.Width:={CountRjad}((Len+1) div 2)*wid;
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
    bmpPole.Width:=Len*wid;
    bmpPole.Height:=wid;
    bmpPole.Canvas.Rectangle(0, 0, bmpPole.Width, bmpPole.Height);
    for i:=1 to Len do begin
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
    Len:=udCount.Position;
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
    for i:=1 to Len do begin
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
begin
    if (X < bmpLeft.Width) then begin
        X:=(X div wid) + 1;
        if (ssLeft in Shift) then begin
            j:=0;
            for i:=1 to CountRjad do
                j:=j + Rjad[i];
            if (X > CountRjad) then begin
                X:=CountRjad + 1;
                if ((j + CountRjad) > Len - 1) then Exit;
                inc(CountRjad);
                Rjad[X]:=0;
            end;
            if ((j + CountRjad) > Len) then Exit;
            Rjad[X]:=Rjad[X] + 1;
        end;
        if (ssRight in Shift) then begin
            if (CountRjad = 0) then Exit;
            if (X > CountRjad) then X:=CountRjad;
            Rjad[X]:=Rjad[X] - 1;
            if (Rjad[X] = 0) then begin
                if (X < CountRjad) then
                    for i:=X to CountRjad do
                        Rjad[i]:=Rjad[i + 1];
                dec(CountRjad);
            end;
        end;

        if (cbMode.Checked) then begin
            for i:=1 to Len do Data[i].Check:=0;
            k:=1; i:=1;
            while (i <= CountRjad) do begin
                for j:=1 to Rjad[i] do
                    Data[k + j - 1].Check:=1;
                Data[k + j].Check:=0;
                k:=k + j;
                inc(i);
            end;
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
    Len:=udCount.Position;
    ClearData;
    GetRjad;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ClearData;
var i:integer;
begin
    for i:=1 to Len do begin
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
    if ((X <= 0) or (X > Len)) then Exit;
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
        if (cbMode.Checked) then GetRjad;
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
    b1, b2:boolean;
    Rjad10:TRjad10;
    cr:integer;
    Del:array [1..100000] of boolean;
begin
    CountComb:=1;

    for i:=1 to 100000 do Del[i]:=true;

    cr:=1; j:=0;
    for i:=1 to CountRjad do begin
        Rjad10[cr].b:=true;
        Rjad10[cr].c:=Rjad[i];
        Rjad10[cr + 1].b:=false;
        Rjad10[cr + 1].c:=1;
        j:=j + Rjad[i] + 1;
        cr:=cr + 2;
    end;
    cr:=cr - 1;
    if (j > Len) then cr:=cr - 1;
    if (j < Len) then Rjad10[cr].c:=Rjad10[cr].c + Len - j;

    GetCombFromRjad(Rjad10, cr, Comb[CountComb]);
    Del[CountComb]:=TestComb(Data, Len, Comb[CountComb]);
    //-------
    b1:=true;
    while (b1) do begin
        inc(CountComb);
        GetRjadFromComb(Rjad10, cr, Comb[CountComb - 1]);
        b1:=ManipuleRjad(Rjad10, cr);
        GetCombFromRjad(Rjad10, cr, Comb[CountComb]);
        Del[CountComb]:=TestComb(Data, Len, Comb[CountComb]);
    end;
    //-----------
    for i:=1 to Len do begin
        Data[i].Check:=0;
        b1:=true;
        b2:=false;
        for j:=1 to CountComb do begin
            if (not Del[j]) then Continue; 
            b1:=b1 and Comb[j, i];
            b2:=b2 or Comb[j, i];
        end;
        if (b1) then Data[i].Check:=1;
        if (not b2) then Data[i].Check:=2;
    end;
    GetRjad;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TForm1.TestComb(var dt: TData; l:integer; var bits: TBitArray): boolean;
var i:integer;
begin
    Result:=true;
    for i:=1 to l do begin
        case dt[i].Check of
            0:;
            1:if (bits[i] <> true) then Result:=false;
            2:if (bits[i] <> false) then Result:=false;
        end;
        if (not Result) then Exit;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetCombFromRjad(var r: TRjad10; var cr:integer; var bits:TBitArray);
var  i, j, x:integer;
begin
    x:=0;
    for i:=1 to cr do begin
        for j:=1 to r[i].c do
            bits[x + j]:=r[i].b;
        x:=x + r[i].c;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjadFromComb(var r: TRjad10; var cr: integer; var bits: TBitArray);
var i, j:integer;
    b:boolean;
begin
    b:=bits[1];
    j:=1; cr:=1;
    for i:=2 to Len do begin
        if (bits[i] xor b) then begin
            r[cr].c:=j;
            r[cr].b:=b;
            inc(cr);
            j:=0;
        end;
        inc(j);
        b:=bits[i];
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
procedure TForm1.cbModeClick(Sender: TObject);
begin
    btCalc.Enabled:=not cbMode.Checked; 
    GetRjad;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
