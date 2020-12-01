unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls, ExtCtrls, Unit2, MyUnit;

type
  TXYData = array [1..40, 1..40] of byte;
  TXYRjad = array [1..40, 1..40] of byte;
  TFinish = array [1..40] of boolean;
  TXYCountRjad = array [1..40] of integer;
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
    procedure DrawRight;
    procedure GetRjadX;
    procedure GetRjadY;
    procedure DataFromRjadX(y:integer);
    procedure DataFromRjadY(x:integer);
    procedure ClearData;
    function  Check:integer;
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
procedure TForm1.Draw;
begin
    DrawPole;
    DrawSmall;
    DrawLeft;
    DrawRight;
    Buf.Width:=bmpLeft.Width + bmpPole.Width;
    Buf.Height:=bmpTop.Height + bmpPole.Height;
    Buf.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpTop.Height);
    Buf.Canvas.Draw(0,             bmpTop.Height, bmpLeft);
    Buf.Canvas.Draw(bmpLeft.Width, 0,             bmpTop);
    Buf.Canvas.Draw(bmpLeft.Width, bmpTop.Height, bmpPole);
    Buf.Canvas.Draw((bmpLeft.Width - bmpSmall.Width) div 2,
                    (bmpTop.Height - bmpSmall.Height) div 2, bmpSmall);
    pb.Height:=Buf.Height;
    pb.Width:=Buf.Width;
    pbPaint(Self);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawLeft;
var x, y, tx, ty, a, px:integer;
    tstr:string;
begin
    a:=((LenX + 1) div 2);
    bmpLeft.Width:={CountRjad}a*wid;
    bmpLeft.Height:=LenY*wid;
    bmpLeft.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpLeft.Height);
    for y:=1 to LenY do begin
        for x:=1 to LenX do bmpLeft.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid, y*wid);
        for x:=1 to CountRjadX[y] do begin
            px:=a - CountRjadX[y] + x;
            tx:=(px - 1)*wid; ty:=(y - 1)*wid;
//            if ((CurrPt.xy) and (px = CurrPt.pt.x) and (y = CurrPt.pt.y))
//                then bmpTop.Canvas.Brush.Color:=clDkGray;
            bmpLeft.Canvas.Rectangle(tx, ty, px*wid, y*wid);
            tstr:=IntToStr(RjadX[x, y]);
            bmpLeft.Canvas.TextOut(tx + (wid - bmpLeft.Canvas.TextWidth(tstr) + 1) div 2,
                                   ty + (wid - bmpLeft.Canvas.TextHeight(tstr)) div 2, tstr);
//            bmpTop.Canvas.Brush.Color:=clWhite;
        end;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawRight;
var x, y, tx, ty, a, py:integer;
    tstr:string;
begin
    a:=((LenY + 1) div 2);
    bmpTop.Width:=LenX*wid;
    bmpTop.Height:=a*wid;
    bmpTop.Canvas.Rectangle(0, 0, bmpTop.Width, bmpTop.Height);
    for x:=1 to LenX do begin
        for y:=1 to LenY do bmpTop.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid, y*wid);
        for y:=1 to CountRjadY[x] do begin
            py:=a - CountRjadY[x] + y;
            tx:=(x - 1)*wid; ty:=(py - 1)*wid;
//            if ((not CurrPt.xy) and (x = CurrPt.pt.x) and (py = CurrPt.pt.y))
//                then bmpTop.Canvas.Brush.Color:=clDkGray;
            bmpTop.Canvas.Rectangle(tx, ty, x*wid, py*wid);
            tstr:=IntToStr(RjadY[x, y]);
            bmpTop.Canvas.TextOut(tx + (wid - bmpTop.Canvas.TextWidth(tstr) + 1) div 2,
                                    ty + (wid - bmpTop.Canvas.TextHeight(tstr)) div 2, tstr);
//            bmpTop.Canvas.Brush.Color:=clWhite;
        end;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawPole;
var x, y:integer;
begin
    bmpPole.Width:=LenX*wid;
    bmpPole.Height:=LenY*wid;
    bmpPole.Canvas.Rectangle(0, 0, bmpPole.Width, bmpPole.Height);
    for x:=1 to LenX do
        for y:=1 to LenY do begin
            case (Data[x, y]) of
                0: bmpPole.Canvas.Brush.Color:=clWhite;
                1: bmpPole.Canvas.Brush.Color:=clBlack;
                2: bmpPole.Canvas.Brush.Color:=clLtGray;
            end;
            bmpPole.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid, y*wid);
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
            bmpSmall.Canvas.Rectangle((x - 1)*w, (y - 1)*w, x*w, y*w);
        end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormCreate(Sender: TObject);
begin
    PredCoord:=Point(-1, -1);
    PredButt:=[ssLeft];
    CurrPt.xy:=false;
    CurrPt.pt:=Point(0, 0);

    LenX:=udCountX.Position;
    LenY:=udCountY.Position;
    bDown:=false;

    CurrPt.pt:=Point(1, 1);
    CurrPt.xy:=true;

    od.InitialDir:=ExtractFileDir(Application.ExeName);
    sd.InitialDir:=od.InitialDir;

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
    bmpLeft.Canvas.Font.Size:=7;

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
    edInput.SetFocus;
    if ((X < bmpLeft.Width) and (Y < bmpTop.Height)) then Exit;
    if (X < bmpLeft.Width) then begin
        Y:=Y - bmpTop.Height;
        X:=X;
        Y:=(Y div wid) + 1;
        X:=(X div wid);

        CurrPt.xy:=true;
        CurrPt.pt.y:=X;
        CurrPt.pt.x:=Y;

        if (Shift = [ssAlt, ssLeft]) then begin
            
        end;
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

        j:=((LenX + 1) div 2);
        X:=X - (j - CountRjadX[Y]) + 1;
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

        j:=((LenY + 1) div 2);
        Y:=Y - (j - CountRjadY[X]) + 1;
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
    LenX:=udCountX.Position;
    LenY:=udCountY.Position;
//    ClearData;
//    GetRjadX;
//    GetRjadY;
    Draw;
    a:=pb.Height + pb.Top + 30;
    if (a < 269)
        then Form1.Height:=269
        else Form1.Height:=pb.Height + pb.Top + 30;
    Form1.Width:=pb.Width + pb.Left + 10;
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
//    if ((PredButt = Shift) and (PredCoord.x = X)) then Exit;
//    if ((PredButt = Shift) and (PredCoord.y = Y)) then Exit;
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
    Result:=Abs(a1 - a2);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btCalcClick(Sender: TObject);
var x, y:integer;
    b, c:boolean;
    t:TdateTime; h, m, s, ms:word;
begin
    if (btCalc.Tag = 0)
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
            Exit;
        end;

    t:=Now;
    Unit2.Max:=0;
    x:=Check;
    if (x <> 0) then begin
        ShowMessage('Ошибка! Несовпадение на ' + IntToStr(x));
        Exit;
    end;
    repeat
        b:=false;
        for y:=1 to LenY do begin
            Application.ProcessMessages;
            if (btCalc.Tag = 0) then Exit;
            if (FinX[y]) then Continue;
            PrepRjadX(y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
            Unit2.glLen:=LenX;
            Unit2.Calculate;
            if (Unit2.glCountComb = 0) then begin
                ShowMessage('Ошибка в кроссворде (строка ' + IntToStr(y) + ').');
                Exit;
            end;
            if (Unit2.glCountRjad = 0) then continue;
            c:=false;
            for x:=1 to LenX do begin
                b:=b or (Form1.Data[x, y] <> Unit2.glData[x]);
                Form1.Data[x, y]:=Unit2.glData[x];
                c:=c or (Form1.Data[x, y] = 0);
            end;
            FinX[y]:=not c;
            Draw;
        end;
        for x:=1 to LenX do begin
            Application.ProcessMessages;
            if (btCalc.Tag = 0) then Exit;
            if (FinY[x]) then Continue;
            PrepRjadY(x, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
            Unit2.glLen:=LenY;
            Unit2.Calculate;
            if (Unit2.glCountComb = 0) then begin
                ShowMessage('Ошибка в кроссворде (столбец ' + IntToStr(x) + ').');
                Exit;
            end;
            if (Unit2.glCountRjad = 0) then Continue;
            c:=false;
            for y:=1 to LenY do begin
                b:=b or (Form1.Data[x, y] <> Unit2.glData[y]);
                Form1.Data[x, y]:=Unit2.glData[y];
                c:=c or (Form1.Data[x, y] = 0);
            end;
            FinY[x]:=not c;
            Draw;
        end;
    until (not b);
    DecodeTime(Now - t, h, m, s, ms);
    Form1.Caption:=IntToStr(m) + '-' + IntToStr(s) + '-' + IntToStr(ms) + '-' + IntToStr(Unit2.Max) ;
    for x:=1 to LenX do FinY[x]:=false;
    for y:=1 to LenY do FinX[y]:=false;
    btCalc.Click;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.PrepRjadX(Y: integer; var Data: TData; var Rjad:TRjad; var CountRjad:integer);
var x:integer;
begin
    for x:=1 to LenX do Data[x]:=Form1.Data[x, Y];
    CountRjad:=CountRjadX[Y];
    for x:=1 to CountRjadX[Y] do Rjad[x]:=Form1.RjadX[x, Y];
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.PrepRjadY(X: integer; var Data: TData; var Rjad:TRjad; var CountRjad:integer);
var y:integer;
begin
    for y:=1 to LenY do Data[y]:=Form1.Data[X, y];
    CountRjad:=CountRjadY[X];
    for y:=1 to CountRjadY[X] do Rjad[y]:=Form1.RjadY[X, y];
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
    udCountX.Position:=StrToInt(tstr);
    ReadLn(F, tstr);
    udCountY.Position:=StrToInt(tstr);
    for y:=1 to LenY do begin
        ReadLn(F, tstr);
        CountRjadX[y]:=StrToInt(tstr);
        for x:=1 to CountRjadX[y] do begin
            ReadLn(F, tstr);
            RjadX[x, y]:=StrToInt(tstr);
        end;
    end;

    for x:=1 to LenX do begin
        ReadLn(F, tstr);
        CountRjadY[x]:=StrToInt(tstr);
        for y:=1 to CountRjadY[x] do begin
            ReadLn(F, tstr);
            RjadY[x, y]:=StrToInt(tstr);
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
    WriteLn(F, IntToStr(LenX));
    WriteLn(F, IntToStr(LenY));
    for y:=1 to LenY do begin
        WriteLn(F, IntToStr(CountRjadX[y]));
        for x:=1 to CountRjadX[y] do
            WriteLn(F, IntToStr(RjadX[x, y]));
    end;

    for x:=1 to LenX do begin
        WriteLn(F, IntToStr(CountRjadY[x]));
        for y:=1 to CountRjadY[x] do
            WriteLn(F, IntToStr(RjadY[x, y]));
    end;
    CloseFile(F);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btSaveClick(Sender: TObject);
var tstr:string;
begin
    if (cbMode.Checked) then sd.FilterIndex:=2 else sd.FilterIndex:=1;
    if (not sd.Execute) then Exit;
    tstr:=ExtractFileExt(sd.FileName);
    if (tstr = '') then sd.FileName:=sd.FileName + '.jap';
    if ((tstr <> '.jap') and ((tstr <> '.jdt'))) then sd.FileName:=ChangeFileExt(sd.FileName, '.jap');
    case (sd.FilterIndex) of
        1: tstr:='.jap';
        2: tstr:='.jdt';
    end;
    sd.FileName:=ChangeFileExt(sd.FileName, tstr);
    if (tstr = '.jap') then SaveRjadToFile(sd.FileName);
    if (tstr = '.jdt') then SaveDataToFile(sd.FileName);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btLoadClick(Sender: TObject);
var tstr:string;
begin
    if (cbMode.Checked) then sd.FilterIndex:=2 else sd.FilterIndex:=1;
    if (not od.Execute) then Exit;
    tstr:=ExtractFileExt(od.FileName);
    if (tstr = '') then od.FileName:=od.FileName + '.jap';
    if ((tstr <> '.jap') and ((tstr <> '.jdt'))) then od.FileName:=ChangeFileExt(od.FileName, '.jap');
    if (tstr = '.jap') then begin
        cbMode.Checked:=false;
        if (cbMode.Checked) then cbMode.Caption:='Редактор' else cbMode.Caption:='Расш.';
        LoadRjadFromFile(od.FileName);
        ClearData;
    end;
    if (tstr = '.jdt') then begin
        cbMode.Checked:=true;
        if (cbMode.Checked) then cbMode.Caption:='Редактор' else cbMode.Caption:='Расш.';
        LoadDataFromFile(od.FileName);
        GetRjadX;
        GetRjady;
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
    udCountX.Position:=StrToInt(tstr);
    ReadLn(F, tstr);
    udCountY.Position:=StrToInt(tstr);
    for x:=1 to LenX do
        for y:=1 to LenY do begin
            ReadLn(F, tstr);
            Data[x ,y]:=StrToInt(tstr);
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
    WriteLn(F, IntToStr(LenX));
    WriteLn(F, IntToStr(LenY));
    for x:=1 to LenX do
        for y:=1 to LenY do
            WriteLn(F, IntToStr(Data[x, y]));
    CloseFile(F);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btClearClick(Sender: TObject);
begin
    ClearData;
    if (cbMode.Checked) then begin
        GetRjadX;
        GetRjadY;
    end;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.edInputKeyPress(Sender: TObject; var Key: Char);
var i, j, a:integer;
    tstr:string;
begin
    if (Key = #13) then begin
        tstr:=edInput.Text;
        if (tstr = '') then Exit;
        if (tstr[Length(tstr)] <> '.') then tstr:=tstr + '.';
        a:=DecodeStrToInt(tstr, '.', 0);
        if (not CurrPt.xy)
            then begin
                CountRjadY[CurrPt.pt.x]:=a;
                for i:=1 to a do
                    RjadY[CurrPt.pt.x, i]:=DecodeStrToInt(tstr, '.', i);

                j:=0;
                for i:=1 to a do
                    j:=j + RjadY[CurrPt.pt.x, i];
                if ((j + a - 1) > LenY) then begin
                    CountRjadY[CurrPt.pt.x]:=0;
                    Beep;
                    Exit;
                end;
                if (cbMode.Checked) then DataFromRjadY(CurrPt.pt.x);
            end
            else begin
                CountRjadX[CurrPt.pt.x]:=a;
                for i:=1 to a do
                    RjadX[i, CurrPt.pt.x]:=DecodeStrToInt(tstr, '.', i);

                j:=0;
                for i:=1 to a do
                    j:=j + RjadX[i, CurrPt.pt.x];
                if ((j + a - 1) > LenX) then begin
                    CountRjadX[CurrPt.pt.x]:=0;
                    Beep;
                    Exit;
                end;
                if (cbMode.Checked) then DataFromRjadX(CurrPt.pt.x);
            end;
        Inc(CurrPt.pt.x);
        if (CurrPt.xy)
            then begin
                if (CurrPt.pt.x > LenY) then begin
                    CurrPt.xy:=false;
                    CurrPt.pt.x:=0;
                end;
            end
            else begin
                if (CurrPt.pt.x > LenX) then begin
                    CurrPt.xy:=true;
                    CurrPt.pt.x:=0;
                end;
            end;
        edInput.Text:='';
        Draw;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
