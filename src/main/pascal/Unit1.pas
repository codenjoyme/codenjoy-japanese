unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls, ExtCtrls, Unit2, MyUnit, ExtDlgs;

type
  TXYData = array [1..MaxLen, 1..MaxLen] of byte;
  TXYRjad = array [1..MaxLen, 1..MaxLen] of byte;
  TFinish = array [1..MaxLen] of boolean;
  TXYCountRjad = array [1..MaxLen] of integer;
  TForm1 = class(TForm)
    edCountX: TEdit;
    udCountX: TUpDown;
    pb: TPaintBox;
    btCalc: TButton;
    cbMode: TCheckBox;
    edCountY: TEdit;
    udCountY: TUpDown;
    btSave: TButton;
    btLoad: TButton;
    od: TOpenDialog;
    sd: TSaveDialog;
    btClear: TButton;
    edInput: TEdit;
    cbRjad: TCheckBox;
    btSaveBimap: TButton;
    spd: TSavePictureDialog;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure pbPaint(Sender: TObject);
    procedure edCountXChange(Sender: TObject);
    procedure pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
    procedure pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure btCalcClick(Sender: TObject);
    procedure cbModeClick(Sender: TObject);
    procedure btSaveClick(Sender: TObject);
    procedure btLoadClick(Sender: TObject);
    procedure btClearClick(Sender: TObject);
    procedure edInputKeyPress(Sender: TObject; var Key: Char);
    procedure cbRjadClick(Sender: TObject);
    procedure btSaveBimapClick(Sender: TObject);
    procedure edInputMouseDown(Sender: TObject; Button: TMouseButton;
      Shift: TShiftState; X, Y: Integer);
  private
    PredCoord:TPoint; PredButt:TShiftState;
    bDown:boolean;
    Buf, bmpTop, bmpLeft, bmpPole, bmpSmall:TBitMap;
    CurrPt:record
        pt:TPoint;
        xy:boolean;
    end;
    FinX, FinY:TFinish;
    Data:TXYData;
    LenX, LenY:integer;
    RjadX, RjadY:TXYRjad;
    CountRjadX, CountRjadY:TXYCountRjad;
    procedure Draw;
    procedure DrawPole;
    procedure DrawSmall;
    procedure DrawLeft;
    procedure DrawTopt;
    procedure GetRjadX;
    procedure GetRjadY;
    procedure DataFromRjadX(y:integer);
    procedure DataFromRjadY(x:integer);
    procedure ClearData;
    function  Check:integer;
    function  GetMaxCountRjadX:integer;
    function  GetMaxCountRjadY:integer;
    procedure PrepRjadX(Y:integer; var Data:TData; var Rjad:TRjad; var CountRjad:integer);
    procedure PrepRjadY(X:integer; var Data:TData; var Rjad:TRjad; var CountRjad:integer);
    procedure SaveRjadToFile(FileName:string);
    procedure LoadRjadFromFile(FileName:string);
    procedure SaveDataToFile(FileName:string);
    procedure LoadDataFromFile(FileName:string);
  public
    { Public declarations }
  end;

var Form1: TForm1;
const wid = 14;
      fs = 7;  
implementation

{$R *.DFM}
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ClearData;
var x, y:integer;
begin
    for x:=1 to LenX do
        for y:=1 to LenY do
            Data[x, y]:=0;
    for x:=1 to LenX do FinY[x]:=false;
    for y:=1 to LenY do FinX[y]:=false;         
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjadX;
var x, y, a:integer;
begin
    for y:=1 to LenY do begin
        a:=0; CountRjadX[y]:=1;
        for x:=1 to LenX do begin
            if (Data[x, y] = 1)
                then inc(a)
                else
                    if (a <> 0) then begin
                        RjadX[CountRjadX[y], y]:=a;
                        a:=0;
                        inc(CountRjadX[y]);
                    end;
        end;
        if (a <> 0) then begin
            RjadX[CountRjadX[y], y]:=a;
            inc(CountRjadX[y]);
        end;
        dec(CountRjadX[y]);
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjadY;
var x, y, a:integer;
begin
    for x:=1 to LenX do begin
        a:=0; CountRjadY[x]:=1;
        for y:=1 to LenY do begin
            if (Data[x, y] = 1)
                then inc(a)
                else
                    if (a <> 0) then begin
                        RjadY[x, CountRjadY[x]]:=a;
                        a:=0;
                        inc(CountRjadY[x]);
                    end;
        end;
        if (a <> 0) then begin
            RjadY[x, CountRjadY[x]]:=a;
            inc(CountRjadY[x]);
        end;
        dec(CountRjadY[x]);
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TForm1.GetMaxCountRjadY: integer;
var x:integer;
begin
    Result:=0;
    for x:=1 to LenX do
        if (Result < CountRjadY[x])
            then Result:=CountRjadY[x];
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TForm1.GetMaxCountRjadX: integer;
var y:integer;
begin
    Result:=0;
    for y:=1 to LenY do
        if (Result < CountRjadX[y])
            then Result:=CountRjadX[y];
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.Draw;
var w, h, a:integer;
begin
    DrawPole;
    if (not cbRjad.Checked) then DrawSmall;
    DrawLeft;
    DrawTopt;
    Buf.Width:=bmpLeft.Width + bmpPole.Width;
    Buf.Height:=bmpTop.Height + bmpPole.Height;
    Buf.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpTop.Height);
    Buf.Canvas.Draw(0,             bmpTop.Height, bmpLeft);
    Buf.Canvas.Draw(bmpLeft.Width, 0,             bmpTop);
    Buf.Canvas.Draw(bmpLeft.Width, bmpTop.Height, bmpPole);
    if (not cbRjad.Checked)
        then Buf.Canvas.Draw((bmpLeft.Width - bmpSmall.Width) div 2,
                             (bmpTop.Height - bmpSmall.Height) div 2, bmpSmall);
    pb.Height:=Buf.Height;
    pb.Width:=Buf.Width;
    pbPaint(Self);

    h:=pb.Height + pb.Top + 30;
    a:=edInput.Top + edInput.Height + 30; 
    if (h < a) then h:=a;
    w:=pb.Width + pb.Left + 10;

    if (Form1.Height <> h) then Form1.Height:=h;     
    if (Form1.Width <> w)  then Form1.Width:=w;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawLeft;
var x, y, tx, ty, a, d, px, w:integer;
    tstr:string;
begin
    w:= wid - 1;
    if (cbRjad.Checked)
        then a:=GetMaxCountRjadX
        else a:=((LenX + 1) div 2);
    bmpLeft.Width:=a*wid + 1;
    bmpLeft.Height:=LenY*wid + 1;
    bmpLeft.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpLeft.Height);
    for y:=1 to LenY do begin
        if ((y mod 5) = 0) then d:=0 else d:=1;
        for x:=1 to LenX do bmpLeft.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid + 1, y*wid + d);
        for x:=1 to CountRjadX[y] do begin
            px:=a - CountRjadX[y] + x;
            tx:=(px - 1)*wid; ty:=(y - 1)*wid;
            bmpLeft.Canvas.Rectangle(tx, ty, px*wid + 1, y*wid + d);
            tstr:=IntToStr(RjadX[x, y]);
            bmpLeft.Canvas.TextOut(tx + (wid - bmpLeft.Canvas.TextWidth(tstr) + 1) div 2,
                                   ty + (wid - bmpLeft.Canvas.TextHeight(tstr)) div 2, tstr);
        end;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawTopt;
var x, y, tx, ty, a, d, py:integer;
    tstr:string;
begin
    if (cbRjad.Checked)
        then a:=GetMaxCountRjadY
        else a:=((LenY + 1) div 2);
    bmpTop.Width:=LenX*wid + 1;
    bmpTop.Height:=a*wid + 1;
    bmpTop.Canvas.Rectangle(0, 0, bmpTop.Width, bmpTop.Height);
    for x:=1 to LenX do begin
        if ((x mod 5) = 0) then d:=0 else d:=1;
        for y:=1 to LenY do bmpTop.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid + d, y*wid + 1);
        for y:=1 to CountRjadY[x] do begin
            py:=a - CountRjadY[x] + y;
            tx:=(x - 1)*wid; ty:=(py - 1)*wid;
            bmpTop.Canvas.Rectangle(tx, ty, x*wid + d, py*wid + 1);
            tstr:=IntToStr(RjadY[x, y]);
            bmpTop.Canvas.TextOut(tx + (wid - bmpTop.Canvas.TextWidth(tstr) + 1) div 2,
                                    ty + (wid - bmpTop.Canvas.TextHeight(tstr)) div 2, tstr);
        end;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawPole;
var x, y, d1, d2:integer;
begin
    bmpPole.Width:=LenX*wid + 1;
    bmpPole.Height:=LenY*wid + 1;
    bmpPole.Canvas.Rectangle(0, 0, bmpPole.Width, bmpPole.Height);
    for x:=1 to LenX do
        for y:=1 to LenY do begin
            if ((x mod 5) = 0) then d1:=0 else d1:=1;
            if ((y mod 5) = 0) then d2:=0 else d2:=1;
            case (Data[x, y]) of
                0: begin
                    bmpPole.Canvas.Brush.Color:=clWhite;
                    bmpPole.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid + d1, y*wid + d2);
                   end;
                1: begin
                    bmpPole.Canvas.Brush.Color:=clLtGray;
                    bmpPole.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid + d1, y*wid + d2);
                    bmpPole.Canvas.Brush.Color:=clBlack;
                    bmpPole.Canvas.Ellipse((x - 1)*wid + 2, (y - 1)*wid + 2, x*wid - 2, y*wid - 2);
                   end;
                2: begin
                    bmpPole.Canvas.Brush.Color:=clLtGray;
                    bmpPole.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid + d1, y*wid + d2);
                   end;
            end;
        end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawSmall;
var x, y, w:integer;
begin
    w:= wid div 3;
    bmpSmall.Width:=LenX*w;
    bmpSmall.Height:=LenY*w;
    bmpSmall.Canvas.Rectangle(0, 0, bmpSmall.Width, bmpSmall.Height);
    for x:=1 to LenX do
        for y:=1 to LenY do begin
            case (Data[x, y]) of
                0, 2: begin
                          bmpSmall.Canvas.Pen.Color:=clWhite;
                          bmpSmall.Canvas.Brush.Color:=clWhite;
                      end;
                1:    begin
                          bmpSmall.Canvas.Pen.Color:=clBlack;
                          bmpSmall.Canvas.Brush.Color:=clBlack;
                      end;
            end;
            bmpSmall.Canvas.Rectangle((x - 1)*w, (y - 1)*w, x*w + 1, y*w + 1);
        end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormCreate(Sender: TObject);
begin
    Application.Title:=Form1.Caption;
    udCountX.Max:=MaxLen;
    udCountY.Max:=MaxLen;
    PredCoord:=Point(-1, -1);
    PredButt:=[ssLeft];
    CurrPt.xy:=true;
    CurrPt.pt:=Point(1, 1);
//    edInput.SetFocus;    

    LenX:=udCountX.Position;
    LenY:=udCountY.Position;
    bDown:=false;

    CurrPt.pt:=Point(1, 1);
    CurrPt.xy:=true;

    od.InitialDir:=ExtractFileDir(Application.ExeName);
    sd.InitialDir:=od.InitialDir;
    spd.InitialDir:=od.InitialDir;

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
    bmpLeft.Canvas.Font.Style:=[fsBold];
    bmpLeft.Canvas.Font.Size:=fs;

    bmpTop:=TBitMap.Create;
    bmpTop.PixelFormat:=pf24bit;
    bmpTop.Width:=10;
    bmpTop.Height:=10;
    bmpTop.Canvas.Pen.Color:=bmpLeft.Canvas.Pen.Color;
    bmpTop.Canvas.Brush.Color:=bmpLeft.Canvas.Brush.Color;
    bmpTop.Canvas.Font.Name:=bmpLeft.Canvas.Font.Name;
    bmpTop.Canvas.Font.Style:=bmpLeft.Canvas.Font.Style;
    bmpTop.Canvas.Font.Size:=bmpLeft.Canvas.Font.Size;

    bmpPole:=TBitMap.Create;
    bmpPole.PixelFormat:=pf24bit;
    bmpPole.Width:=10;
    bmpPole.Height:=10;
    bmpPole.Canvas.Pen.Color:=clBlack;
    bmpPole.Canvas.Brush.Color:=clWhite;

    bmpSmall:=TBitMap.Create;
    bmpSmall.PixelFormat:=pf24bit;
    bmpSmall.Width:=10;
    bmpSmall.Height:=10;
    bmpSmall.Canvas.Pen.Color:=clBlack;
    bmpSmall.Canvas.Brush.Color:=clWhite;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormDestroy(Sender: TObject);
begin
    Buf.Free;
    bmpLeft.Free;
    bmpTop.Free;
    bmpPole.Free;
    bmpSmall.Free;    
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
var j, k, tx, ty:integer;
begin

    Shift:=Shift - [ssDouble];
    if (edInput.Enabled) then edInput.SetFocus;
    if ((X < bmpLeft.Width) and (Y < bmpTop.Height)) then Exit;
    if (X < bmpLeft.Width) then begin
        if (cbRjad.Checked) then Exit;
        Y:=Y - bmpTop.Height;
        X:=X;
        Y:=(Y div wid) + 1;
        X:=(X div wid);

        CurrPt.xy:=true;
        CurrPt.pt.y:=X;
        CurrPt.pt.x:=Y;

        if (Shift = [ssAlt, ssLeft]) then begin
            PrepRjadX(Y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
            Unit2.glLen:=LenX;
            Unit2.Calculate;
            if (Unit2.glCountComb = 0) then begin
                ShowMessage('Ошибка в кроссворде (строка ' + IntToStr(Y) + ').');
                Exit;
            end;
            if (Unit2.glCountRjad = 0) then Exit;
            for x:=1 to LenX do
                Form1.Data[x, y]:=Unit2.glData[x];
            Draw;
            Exit;
        end;

        if (cbRjad.Checked)
            then X:=X - 1
            else X:=X - (((LenX + 1) div 2) - CountRjadX[Y]) + 1;
        if (Shift = [ssCtrl, ssLeft]) then begin
            for ty:=(Y + 1) to LenY do begin
                CountRjadX[ty - 1]:=CountRjadX[ty];
                for tx:=1 to CountRjadX[ty] do RjadX[tx, ty - 1]:=RjadX[tx, ty];
            end;
            CountRjadX[LenY]:=0;
        end;
        if (Shift = [ssCtrl, ssRight]) then begin
            for ty:=(LenY) downto Y do begin    // аозможен глюк
                CountRjadX[ty]:=CountRjadX[ty - 1];
                for tx:=1 to CountRjadX[ty] do RjadX[tx, ty]:=RjadX[tx, ty - 1];
            end;
            CountRjadX[Y]:=0;
        end;
        if (Shift = [ssLeft]) then begin
            j:=0;
            for tx:=1 to CountRjadX[Y] do
                j:=j + RjadX[tx, Y];
            if (X <= 0) then begin
                if ((j + CountRjadX[Y]) > (LenX - 1)) then Exit;
                for tx:=CountRjadX[Y] downto 1 do RjadX[tx + 1, Y]:=RjadX[tx, Y];
                X:=1;
                inc(CountRjadX[Y]);
                RjadX[X, Y]:=0;
            end;
            if ((j + CountRjadX[Y]) > LenX) then Exit;
            RjadX[X, Y]:=RjadX[X, Y] + 1;
        end;
        if (Shift = [ssRight]) then begin
            if (CountRjadX[Y] = 0) then Exit;
            if (X <= 0) then X:=1;
            RjadX[X, Y]:=RjadX[X, Y] - 1;
            if (RjadX[X, Y] = 0) then begin
                if (X < CountRjadX[Y]) then
                    for tx:=X to CountRjadX[Y] do
                        RjadX[tx, Y]:=RjadX[tx + 1, Y];
                dec(CountRjadX[Y]);
            end;
        end;

        if (cbMode.Checked) then DataFromRjadX(Y);

        Draw;
        Exit;
    end;
    if (Y < bmpTop.Height) then begin
        if (cbRjad.Checked) then Exit;
        Y:=Y;
        X:=X - bmpLeft.Width;
        Y:=(Y div wid);
        X:=(X div wid) + 1;

        CurrPt.xy:=false;
        CurrPt.pt.x:=X;
        CurrPt.pt.y:=Y;

        if (Shift = [ssAlt, ssLeft]) then begin
            PrepRjadY(X, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
            Unit2.glLen:=LenX;
            Unit2.Calculate;
            if (Unit2.glCountComb = 0) then begin
                ShowMessage('Ошибка в кроссворде (столбец ' + IntToStr(X) + ').');
                Exit;
            end;
            if (Unit2.glCountRjad = 0) then Exit;
            for y:=1 to LenY do
                Form1.Data[x, y]:=Unit2.glData[y];
            Draw;
            Exit;
        end;

        if (cbRjad.Checked)
            then Y:=Y - 1
            else Y:=Y - (((LenY + 1) div 2) - CountRjadY[X]) + 1;
        if (Shift = [ssCtrl, ssLeft]) then begin
            for tx:=(X + 1) to LenX do begin
                CountRjadY[tx - 1]:=CountRjadY[tx];
                for ty:=1 to CountRjadY[tx] do RjadY[tx - 1, ty]:=RjadY[tx, ty];
            end;
            CountRjadY[LenX]:=0;
        end;
        if (Shift = [ssCtrl, ssRight]) then begin
            for tx:=(LenX) downto X do begin    // аозможен глюк
                CountRjadY[tx]:=CountRjadY[tx - 1];
                for ty:=1 to CountRjadY[tx] do RjadY[tx, ty]:=RjadY[tx - 1, ty];
            end;
            CountRjadY[X]:=0;
        end;
        if (Shift = [ssLeft]) then begin
            j:=0;                                                                
            for ty:=1 to CountRjadY[X] do                                        
                j:=j + RjadY[X, ty];                                             
            if (Y <= 0) then begin                                               
                if ((j + CountRjadY[X]) > (LenY - 1)) then Exit;                 
                for ty:=CountRjadY[X] downto 1 do RjadY[X, ty + 1]:=RjadY[X, ty];
                Y:=1;                                                            
                inc(CountRjadY[X]);
                RjadY[X, Y]:=0;
            end;
            if ((j + CountRjadY[X]) > LenY) then Exit;                           
            RjadY[X, Y]:=RjadY[X, Y] + 1;                                        
        end;                                                                     
        if (Shift = [ssRight]) then begin
            if (CountRjadY[X] = 0) then Exit;
            if (Y <= 0) then Y:=1;
            RjadY[X, Y]:=RjadY[X, Y] - 1;
            if (RjadY[X, Y] = 0) then begin
                if (Y < CountRjadY[X]) then
                    for ty:=Y to CountRjadY[X] do
                        RjadY[X, ty]:=RjadY[X, ty + 1];
                dec(CountRjadY[X]);
            end;
        end;

        if (cbMode.Checked) then DataFromRjadY(X);

        Draw;
        Exit;
    end;
    bDown:=true;
    pbMouseMove(Sender, Shift, X, Y);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DataFromRjadX(y: integer);
var k, j, tx:integer;
begin
    for tx:=1 to LenX do Data[tx, Y]:=0;
    k:=1; tx:=1;
    while (tx <= CountRjadX[Y]) do begin
        for j:=1 to RjadX[tx, y] do
            Data[k + j - 1, y]:=1;
        Data[k + j, y]:=0;
        k:=k + j;
        inc(tx);
    end;
    GetRjadY;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DataFromRjadY(x: integer);
var k, j, ty:integer;
begin
    for ty:=1 to LenY do Data[x, ty]:=0;
    k:=1; ty:=1;
    while (ty <= CountRjadY[x]) do begin
        for j:=1 to RjadY[x, ty] do
            Data[x, k + j - 1]:=1;
        Data[x, k + j]:=0;
        k:=k + j;
        inc(ty);
    end;
    GetRjadX;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbPaint(Sender: TObject);
begin
    pb.Canvas.Draw(0, 0, Buf);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.edCountXChange(Sender: TObject);
var a:integer;
begin
    CurrPt.xy:=true;
    CurrPt.pt:=Point(1, 1);
    LenX:=udCountX.Position;
    LenY:=udCountY.Position;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
var bDraw:boolean;
begin
    if (not bDown) then Exit;
    Y:=Y - bmpTop.Height;
    X:=X - bmpLeft.Width;
    Y:=(Y div wid) + 1;
    X:=(X div wid) + 1;
    if ((Y <= 0) or (Y > LenY)) then Exit;
    if ((X <= 0) or (X > LenX)) then Exit;
    bDraw:=false;
    if (ssLeft in Shift) then begin
        bDraw:=(Data[X, Y] <> 1);
        Data[X, Y]:=1
    end;
    if (ssRight in Shift) then begin
        bDraw:=(Data[X, Y] <> 0);
        Data[X, Y]:=0;
    end;
    if (ssMiddle in Shift) then begin
        bDraw:=(Data[X, Y] <> 2);
        Data[X, Y]:=2;
    end;
    PredCoord:=Point(X, Y);
    PredButt:=Shift;
    if (bDraw) then begin
        if (cbMode.Checked) then begin
            GetRjadX;
            GetRjadY;
        end;
        Draw;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
begin
    bDown:=false;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.cbModeClick(Sender: TObject);
begin
    btCalc.Enabled:=not cbMode.Checked;
    if (cbMode.Checked) then cbMode.Caption:='Редактор' else cbMode.Caption:='Расш.';
    if (cbMode.Checked) then begin
        GetRjadX;
        GetRjadY;
    end;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TForm1.Check:integer;
var x, y, a1, a2:integer;
begin
    a1:=0;
    for x:=1 to LenX do
        for y:=1 to CountRjadY[x] do a1:=a1 + RjadY[x, y];
    a2:=0;
    for y:=1 to LenY do
        for x:=1 to CountRjadX[y] do a2:=a2 + RjadX[x, y];
    Result:=Abs(a1 - a2);  // разница рядов
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btCalcClick(Sender: TObject);
var x, y:integer;
    b, c:boolean;
    t:TdateTime; h, m, s, ms:word;
begin
    if (btCalc.Tag = 0) // интерфейсные изменение Остановить-Расчет               
        then begin
            btCalc.Caption:='&Остановить';
            btCalc.Tag:=1;
            udCountX.Enabled:=false;
            udCountY.Enabled:=false;
            cbMode.Enabled:=false;
            btSave.Enabled:=false;
            btLoad.Enabled:=false;
            btClear.Enabled:=false;
            edInput.Enabled:=false;
        end
        else begin
            btCalc.Caption:='&Расчет';
            btCalc.Tag:=0;
            udCountX.Enabled:=true;
            udCountY.Enabled:=true;
            cbMode.Enabled:=true;
            btSave.Enabled:=true;
            btLoad.Enabled:=true;
            btClear.Enabled:=true;
            edInput.Enabled:=true;
            Exit; // сразу выходим
        end;

    t:=Now; //
    Unit2.Max:=0; //

    x:=Check; // проверка на совпадение рядов
    if (x <> 0) then begin
        ShowMessage('Ошибка! Несовпадение на ' + IntToStr(x));
        btCalc.Click; // остановка
        Exit;
    end;
    // сам рачсет
    repeat
        b:=false;
        for y:=1 to LenY do begin
            Application.ProcessMessages;  // передышка
            if (btCalc.Tag = 0) then Exit; // если остановили

            if (FinX[y]) then Continue; // если строка закончена
            PrepRjadX(y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad); // подготовка строки 
            Unit2.glLen:=LenX; // длинна строки
            if (not Unit2.Calculate) then begin // расчет ... если нет ни одной комбины - ошибка
                ShowMessage('Ошибка в кроссворде (строка ' + IntToStr(y) + ').');
                btCalc.Click; // остановка
                Exit;
            end;
            // заполнение поля
            c:=false; // флаг закончености строки
            for x:=1 to LenX do begin // по строке
                b:=b or (Form1.Data[x, y] <> Unit2.glData[x]); // если произошли изменения
                Form1.Data[x, y]:=Unit2.glData[x]; // запись
                c:=c or (Form1.Data[x, y] = 0); // если заполнено
            end;
            FinX[y]:=not c; // массив флагов заполнености
        end;
        Draw; // прорисовка
        // дальше то же только для столбцов
        for x:=1 to LenX do begin
            Application.ProcessMessages;
            if (btCalc.Tag = 0) then Exit;
            if (FinY[x]) then Continue;
            PrepRjadY(x, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
            Unit2.glLen:=LenY;
            if (not Unit2.Calculate) then begin // расчет ... если нет ни одной комбины - ошибка
                ShowMessage('Ошибка в кроссворде (столбец ' + IntToStr(x) + ').');
                btCalc.Click;
                Exit;
            end;
            c:=false;
            for y:=1 to LenY do begin
                b:=b or (Form1.Data[x, y] <> Unit2.glData[y]);
                Form1.Data[x, y]:=Unit2.glData[y];
                c:=c or (Form1.Data[x, y] = 0);
            end;
            FinY[x]:=not c;
        end;
        Draw;
    until (not b);
    DecodeTime(Now - t, h, m, s, ms);  //
    Form1.Caption:=IntToStr(m) + '-' + IntToStr(s) + '-' + IntToStr(ms) + '-' + IntToStr(Unit2.Max) ;  //
    // очистка массивв флагов заполнености
    for x:=1 to LenX do FinY[x]:=false;
    for y:=1 to LenY do FinX[y]:=false;
    btCalc.Click; // остановка
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.PrepRjadX(Y: integer; var Data: TData; var Rjad:TRjad; var CountRjad:integer);
var x:integer;
begin
    // подготовка строки
    for x:=1 to LenX do Data[x]:=Form1.Data[x, Y]; // данные
    CountRjad:=CountRjadX[Y]; // длинна ряда
    for x:=1 to CountRjadX[Y] do Rjad[x]:=Form1.RjadX[x, Y];  // сам ряд
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.PrepRjadY(X: integer; var Data: TData; var Rjad:TRjad; var CountRjad:integer);
var y:integer;
begin
    // подготовка столбца
    for y:=1 to LenY do Data[y]:=Form1.Data[X, y];  // данные
    CountRjad:=CountRjadY[X]; // длинна ряда
    for y:=1 to CountRjadY[X] do Rjad[y]:=Form1.RjadY[X, y]; // сам ряд
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.LoadRjadFromFile(FileName: string);
var x, y:integer;
    F:TextFile;
    tstr:string;
begin
    AssignFile(F, FileName);
    ReSet(F);
    ReadLn(F, tstr);
    udCountX.Position:=StrToInt(tstr); // ширина
    ReadLn(F, tstr);
    udCountY.Position:=StrToInt(tstr); // высота
    for y:=1 to LenY do begin
        ReadLn(F, tstr);
        CountRjadX[y]:=StrToInt(tstr); // длинна y строки
        for x:=1 to CountRjadX[y] do begin
            ReadLn(F, tstr);
            RjadX[x, y]:=StrToInt(tstr); // числа y строки
        end;
    end;

    for x:=1 to LenX do begin
        ReadLn(F, tstr);
        CountRjadY[x]:=StrToInt(tstr);       // длинна х столбца
        for y:=1 to CountRjadY[x] do begin
            ReadLn(F, tstr);
            RjadY[x, y]:=StrToInt(tstr);   // числа х столбца
        end;
    end;
    CloseFile(F);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.SaveRjadToFile(FileName: string);
var x, y:integer;
    F:TextFile;
begin
    AssignFile(F, FileName);
    ReWrite(F);
    WriteLn(F, IntToStr(LenX));  // ширина
    WriteLn(F, IntToStr(LenY));  // высота
    for y:=1 to LenY do begin
        WriteLn(F, IntToStr(CountRjadX[y])); // длинна y строки
        for x:=1 to CountRjadX[y] do
            WriteLn(F, IntToStr(RjadX[x, y])); // числа y строки
    end;

    for x:=1 to LenX do begin
        WriteLn(F, IntToStr(CountRjadY[x]));  // длинна х столбца
        for y:=1 to CountRjadY[x] do
            WriteLn(F, IntToStr(RjadY[x, y]));  // числа х столбца
    end;
    CloseFile(F);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btSaveClick(Sender: TObject);
var tstr, ext:string;
begin
    if (cbMode.Checked) // расширение по умолчанию (2 - файло редактора)
        then begin
            sd.FilterIndex:=2;
            ext:='.jdt';
        end
        else begin
            sd.FilterIndex:=1;
            ext:='.jap';
        end;
    if (not sd.Execute) then Exit; // запуск диалога
    tstr:=ExtractFileExt(sd.FileName); // расширение
    if (tstr = '') then sd.FileName:=sd.FileName + ext; // если нет разрешения то разрешение по умолчанию
    if ((tstr <> '.jap') and ((tstr <> '.jdt'))) // если не те расширения ...
        then sd.FileName:=ChangeFileExt(sd.FileName, ext); //... то по умолчанию
    // грузим файл
    case (sd.FilterIndex) of
        1: SaveRjadToFile(sd.FileName);
        2: SaveDataToFile(sd.FileName);
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btLoadClick(Sender: TObject);
var tstr, ext:string;
begin
    if (cbMode.Checked) // расширение по умолчанию (2 - файло редактора jdt)
        then begin
            od.FilterIndex:=2;
            ext:='.jdt';
        end
        else begin
            od.FilterIndex:=1;
            ext:='.jap';
        end;
    if (not od.Execute) then Exit; // запуск диалога
    tstr:=ExtractFileExt(od.FileName); // имя файла
    if (tstr = '') then od.FileName:=od.FileName + ext;// если нет разрешения то разрешение по умолчанию
    if ((tstr <> '.jap') and ((tstr <> '.jdt'))) // если не те расширения ...
        then od.FileName:=ChangeFileExt(od.FileName, ext); //... то по умолчанию
    if (not FileExists(od.FileName)) then Exit;
    case (od.FilterIndex) of
        1: begin // файл расшифровщика
            // перекл режим
            cbMode.Checked:=false;
            if (cbMode.Checked) then cbMode.Caption:='Редактор' else cbMode.Caption:='Расш.';
            LoadRjadFromFile(od.FileName); // грузим файл
            ClearData; // очищаем проле
        end;
        2: begin // файл редактора
            // перекл режим
            cbMode.Checked:=true;
            if (cbMode.Checked) then cbMode.Caption:='Редактор' else cbMode.Caption:='Расш.';
            LoadDataFromFile(od.FileName);  // грузим файл
            GetRjadX; // получаем ряды
            GetRjady;
        end;
    end;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.LoadDataFromFile(FileName: string);
var x, y:integer;
    F:TextFile;
    tstr:string;
begin
    AssignFile(F, FileName);
    ReSet(F);
    ReadLn(F, tstr);
    udCountX.Position:=StrToInt(tstr); // ширина
    ReadLn(F, tstr);
    udCountY.Position:=StrToInt(tstr); // высота
    for x:=1 to LenX do
        for y:=1 to LenY do begin
            ReadLn(F, tstr);
            Data[x ,y]:=StrToInt(tstr); // поле
        end;
    CloseFile(F);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.SaveDataToFile(FileName: string);
var x, y:integer;
    F:TextFile;
begin
    AssignFile(F, FileName);
    ReWrite(F);
    WriteLn(F, IntToStr(LenX)); // ширина
    WriteLn(F, IntToStr(LenY)); // высора
    for x:=1 to LenX do
        for y:=1 to LenY do
            WriteLn(F, IntToStr(Data[x, y])); // поле
    CloseFile(F);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btClearClick(Sender: TObject);
begin
    ClearData; // очищаем поле
    CurrPt.xy:=true;
    CurrPt.pt:=Point(1, 1);
    if (cbMode.Checked) then begin // если редактор
        GetRjadX; // то очищаем и цифры
        GetRjadY;
    end;
    Draw; // прорисовка
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.edInputKeyPress(Sender: TObject; var Key: Char);
var i, j, a:integer;
    tstr:string;
begin
    if (Key = #13) then begin
        tstr:=edInput.Text;
        if (tstr = '') then Exit;
        // убираем дубл. точки
        i:=2;
        repeat
            if ((tstr[i - 1] = '.') and (tstr[i] = '.'))
                then tstr:=copy(tstr, 1, i - 1) + copy(tstr, i + 1, Length(tstr) - i)
                else inc(i);
            a:=Length(tstr);
        until (i > a);
        // убираем точку спереди, добавляем в зад 
        if (tstr[Length(tstr)] <> '.') then tstr:=tstr + '.';
        if (tstr[1] = '.') then tstr:=copy(tstr, 2, Length(tstr) - 1);
        if (tstr = '') then Exit; // выходим если пусто
        a:=DecodeStrToInt(tstr, '.', 0); // количество цифер
        if (not CurrPt.xy)
            then begin // столбцы
                {заполнение}
                CountRjadY[CurrPt.pt.x]:=a;
                for i:=1 to a do
                    RjadY[CurrPt.pt.x, i]:=DecodeStrToInt(tstr, '.', i);
                {проверка на ввод нулей - они не нужны}
                i:=1;
                while (i <= a) do
                    if (RjadY[CurrPt.pt.x, i] = 0)
                        then begin
                            if (i <> a) then begin
                                for j:=(i + 1) to a do begin
                                    RjadY[CurrPt.pt.x, j - 1]:=RjadY[CurrPt.pt.x, j];
                                end;
                            end;
                           Dec(CountRjadY[CurrPt.pt.x]);
                           Dec(a);
                        end
                        else inc(i);
                if (CountRjadY[CurrPt.pt.x] = 0) then Exit;
                {проверка на ввод чила большего чем ширина}
                j:=0;
                for i:=1 to a do
                    j:=j + RjadY[CurrPt.pt.x, i];
                if ((j + a - 1) > LenY) then begin
                    CountRjadY[CurrPt.pt.x]:=0;
                    Beep;
                    Exit;
                end;
                {прорисовать на поле если надо}
                if (cbMode.Checked) then DataFromRjadY(CurrPt.pt.x);
            end
            else begin // строки
                {заполнение}
                CountRjadX[CurrPt.pt.x]:=a;
                for i:=1 to a do
                    RjadX[i, CurrPt.pt.x]:=DecodeStrToInt(tstr, '.', i);
                {проверка на ввод нулей - они не нужны}
                i:=1;
                while (i <= a) do
                    if (RjadX[i, CurrPt.pt.x] = 0)
                        then begin
                            if (i <> a) then begin
                                for j:=(i + 1) to a do begin
                                    RjadX[j - 1, CurrPt.pt.x]:=RjadX[j, CurrPt.pt.x];
                                end;
                            end;
                           Dec(CountRjadX[CurrPt.pt.x]);
                           Dec(a);
                        end
                        else inc(i);
                if (CountRjadX[CurrPt.pt.x] = 0) then Exit;
                {проверка на ввод чила большего чем ширина}
                j:=0;
                for i:=1 to a do
                    j:=j + RjadX[i, CurrPt.pt.x];
                if ((j + a - 1) > LenX) then begin
                    CountRjadY[CurrPt.pt.x]:=0;
                    Beep;
                    Exit;
                end;
                {прорисовать на поле если надо}
                if (cbMode.Checked) then DataFromRjadX(CurrPt.pt.x);
            end;
        //следующая ячейка
        Inc(CurrPt.pt.x);
        if (CurrPt.xy)
            then begin
                if (CurrPt.pt.x > LenY) then begin
                    CurrPt.xy:=false;
                    CurrPt.pt.x:=1;
                end;
            end
            else begin
                if (CurrPt.pt.x > LenX) then begin
                    CurrPt.xy:=true;
                    CurrPt.pt.x:=1;
                end;
            end;
        // очищаем едит и прорисовываем
        edInput.Text:='';
        Draw;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.cbRjadClick(Sender: TObject);
begin
    Draw;
    edCountXChange(Self);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btSaveBimapClick(Sender: TObject);
var tstr:string;
begin
    if (not spd.Execute) then Exit;
    cbRjad.Checked:=true;
    tstr:=ExtractFileExt(spd.FileName); // расширение
    if (tstr = '') then spd.FileName:=spd.FileName + '.bmp'; // если нет разрешения то разрешение по умолчанию
    if (tstr <> '.bmp') then spd.FileName:=ChangeFileExt(spd.FileName, '.bmp'); //если не те расширения то по умолчанию
    Buf.SaveToFile(spd.FileName);
    cbRjad.Checked:=false;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.edInputMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
begin
    if (Shift <> [ssLeft, ssAlt, ssCtrl, ssShift]) then Exit;
//    PlaySound(PChar('BUNGA'), Hinstance, SND_RESOURCE or SND_ASYNC or SND_LOOP);
    ShowMessage('Эту программу создал СанЁк (Gerda).' + #$0D + #$0A +
                'Ксюша, если ты сейчас читаешь это сообщение, знай - я Тебя очень сильно люблю, жить без Тебя не могу, Ты у меня самая-самая.' + #$0D + #$0A +
                'Твой на веки.' + #$0D + #$0A + 'Саша.' + #$0D + #$0A + '24 января 2005 года.' + #$0D + #$0A +
                'З.Ы. Хочу, воспользовавшись моментом, передать привет, конечно же, моей женушке Ксюне, мамам, ксюшиному папе, хочу вспомнить своего папу,' + #$0D + #$0A +
                'без которого небыло бы меня, со всеми вытекающими последствиями, сестре Ире и Дане, друзьям Илюхе (Raze), Сопе, Ваську,' + #$0D + #$0A +
                'Вадиму, Олегу, Игорю, Димку, Андрюхе (Хмелу), Ваську (Гуз), Саше,' + #$0D + #$0A + 'пол-групе нормальных человек: Игорю (Юк), Коле, Серому (Толстый),' + #$0D + #$0A +
                'Дене (ЛСД), Веталю, коллективу Укртелеком: Леше, Диме, Лиле, Саше, Шурику, Юле, Ире, однокласникам: Жене, Серому (Попеску), Серому (Шляпе),' + #$0D + #$0A +
                'Оле, Наде, Наташе, Саше (Буля), компьютерке "Компьютер+": Руслану, Вове и Толику, которые собираль мой комп, 5 и 9 школе где я долго зависал в компьютерных классах,' + #$0D + #$0A +
                'и всем всем, кто меня знает и уважает, но кого я не смог вспомнить... Пользуйтесь программой на здоровье, я буду только рад...');
//    PlaySound(nil, 0, 0);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
