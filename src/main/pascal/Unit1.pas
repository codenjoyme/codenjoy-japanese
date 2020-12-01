unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls, ExtCtrls, Unit2, MyUnit, ExtDlgs, Word97, OleServer;

type
  TXYData = array [1..MaxLen, 1..MaxLen] of byte;
  TXYPustot = array [1..MaxLen, 1..MaxLen] of boolean;
  TXYRjad = array [1..MaxLen, 1..MaxLen] of byte;
  TFinish = array [1..MaxLen] of boolean;
  TXYCountRjad = array [1..MaxLen] of integer;
  TXYVer = array [1..MaxLen, 1..MaxLen, 1..2] of Real;
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
    btSaveBitmap: TButton;
    spd: TSavePictureDialog;
    Button1: TButton;
    WordApplication1: TWordApplication;
    WordDocument1: TWordDocument;
    Panel1: TPanel;
    Label1: TLabel;
    cbVerEnable: TCheckBox;
    cbLoadNaklad: TCheckBox;
    CheckBox1: TCheckBox;
    gbInfo: TGroupBox;
    Label2: TLabel;
    Label3: TLabel;
    Label4: TLabel;
    Label5: TLabel;
    Memo1: TMemo;
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
    procedure btSaveBitmapClick(Sender: TObject);
    procedure edInputMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure Button1Click(Sender: TObject);
    procedure udCountXChangingEx(Sender: TObject; var AllowChange: Boolean; NewValue: Smallint; Direction: TUpDownDirection);
    procedure FormMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
    procedure CheckBox1Click(Sender: TObject);
  private
    t:TdateTime;
    PredCoord:TPoint; PredButt:TShiftState;
    bDown:boolean;
    Buf, bmpTop, bmpLeft, bmpPole, bmpSmall:TBitMap;
    CurrPt:record
        pt:TPoint;
        xy:boolean;
    end;
    bChangeLen, bUpDown:boolean;
    Ver:TXYVer;
    FinX, FinY:TFinish;
    ChX, ChY:TFinish;
    Data:TXYData;
    {данные для востановления}
    tChX, tChY:TFinish;
    bPredpl:boolean;
    Predpl:record
        Pustot:TXYPustot;
        NoSet:TXYPustot;
        SetTo:TPoint;
        SetDot:byte;
        bPredpl:boolean;
        FinX, FinY:TFinish;
        ChX, ChY:TFinish;
//v        Ver:TXYVer;
    end;
    {------------------------}
    LenX, LenY:integer;
    RjadX, RjadY:TXYRjad;
    CountRjadX, CountRjadY:TXYCountRjad;
    procedure Draw(pt:TPoint);
    procedure RefreshPole;
    procedure DrawPole(pt:TPoint);
    procedure DrawSmall;
    procedure DrawLeft(yy:integer);
    procedure DrawTop(xx:integer);
    procedure GetRjadX;
    procedure GetRjadY;
    procedure DataFromRjadX(y:integer);
    procedure DataFromRjadY(x:integer);
    procedure ClearData(all:boolean = true);
    function  GetFin:boolean;
    function  Check:TPoint;
    function  GetMaxCountRjadX:integer;
    function  GetMaxCountRjadY:integer;
    procedure PrepRjadX(Y:integer; var Data:TData; var Rjad:TRjad; var CountRjad:integer);
    procedure PrepRjadY(X:integer; var Data:TData; var Rjad:TRjad; var CountRjad:integer);
    procedure SaveRjadToFile(FileName:string);
    procedure LoadRjadFromFile(FileName:string);
    procedure SaveDataToFile(FileName:string);
    procedure LoadDataFromFile(FileName:string);
    procedure SavePustot(pt:TPoint);
    function  LoadPustot:TPoint;
    procedure SetPredplDot(bDot:boolean);
    procedure SetInfo(Rjad: integer; bRjadStolb, bPredpl, bTimeNow: boolean; bWhoPredpl:byte);
  public
    { Public declarations }
  end;

var Form1: TForm1;
const wid = 10;
      fs = 5;
implementation

{$R *.DFM}
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.ClearData(all:boolean = true);
var x, y:integer;
begin
    for x:=1 to MaxLen do begin
        for y:=1 to MaxLen do begin
            Data[x, y]:=0;
            Ver[x, y, 1]:=-1;
            Ver[x, y, 2]:=-1;
//            RjadX[x, y]:=0;
//            RjadY[x, y]:=0;
        end;
        if (all) then begin
            CountRjadX[x]:=0;
            CountRjadY[x]:=0;
        end;
        FinY[x]:=false;
        FinX[x]:=false;
        ChY[x]:=true;
        ChX[x]:=true;
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.GetRjadX;
var x, y, a:integer;
begin
    for y:=1 to {LenY}MaxLen do begin
        a:=0; CountRjadX[y]:=1;
        for x:=1 to {LenX}MaxLen do begin
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
    for x:=1 to {LenX}MaxLen do begin
        a:=0; CountRjadY[x]:=1;
        for y:=1 to {LenY}MaxLen do begin
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
procedure TForm1.Draw(pt:TPoint);
var w, h, a:integer;
    b:boolean;
    //----
    procedure ResPanel;
    begin
        h:=pb.Height + 2;
        w:=pb.Width + 2;
        if (Panel1.Height <> h) then Panel1.Height:=h;
        if (Panel1.Width <> w) then Panel1.Width:=w;
    end;
    //----
    procedure ResForm;
    begin
        h:=pb.Height + Panel1.Top + 30 + 15;
        a:=gbInfo.Top + gbInfo.Height + 32;
        if (h < a) then h:=a;
        w:=pb.Width + Panel1.Left + 11 + 15;
        Form1.Constraints.MinHeight:=0;
        Form1.Constraints.MinWidth:=0;
        if (w < (Screen.Width - 20)) then begin
            if (b) then Form1.Width:=w;
            Form1.Constraints.MinWidth:=w;
        end;
        if (h < (Screen.Height - 100)) then begin
            if (b) then Form1.Height:=h;
            Form1.Constraints.MinHeight:=h;
        end;
    end;
    //----
begin
    if (not cbRjad.Checked) then DrawSmall;
    if (pt.x <> -1) then begin
        DrawPole(pt);
        DrawLeft(pt.y);
        DrawTop(pt.x);
    end;
    Buf.Width:=bmpLeft.Width + bmpPole.Width;
    Buf.Height:=bmpTop.Height + bmpPole.Height;
    Buf.Canvas.Rectangle(0, 0, Buf.Width, Buf.Height);
    Buf.Canvas.Draw(0,             bmpTop.Height, bmpLeft);
    Buf.Canvas.Draw(bmpLeft.Width, 0,             bmpTop);
    Buf.Canvas.Draw(bmpLeft.Width, bmpTop.Height, bmpPole);
    if (not cbRjad.Checked)
        then Buf.Canvas.Draw((bmpLeft.Width - bmpSmall.Width) div 2,
                             (bmpTop.Height - bmpSmall.Height) div 2, bmpSmall);
    pb.Height:=Buf.Height;
    pb.Width:=Buf.Width;
    pbPaint(Self);

    b:=(Form1.WindowState <> wsMaximized);

    if (not bChangeLen) then Exit;

    if (bUpDown)
        then begin
            ResForm;
            ResPanel;
        end
        else begin
            ResPanel;
            ResForm;
        end;

{    if (Form1.VertScrollBar.Visible) then begin
        Form1.Width:=Form1.Width + 15;
        if (not Form1.VertScrollBar.Visible) then Form1.Width:=Form1.Width - 15;
    end;
    if (Form1.HorzScrollBar.Visible) then begin
        Form1.Height:=Form1.Height + 15;
        if (not Form1.HorzScrollBar.Visible) then Form1.Height:=Form1.Height - 15;
    end;
}
    if (b) then begin
        Form1.Left:=(Screen.Width - Form1.Width) div 2;
        Form1.Top:=(Screen.Height - Form1.Height) div 2;
    end;
    bChangeLen:=false;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawLeft(yy:integer);
var x, y, tx, ty, a, d, px, w, yy1, yy2:integer;
    tstr:string;
begin
    w:= wid - 1;
    if (cbRjad.Checked)
        then begin
            d:=(bmpLeft.Width - 1) div wid;
            a:=GetMaxCountRjadX;
            if (a <> d) then yy:=0;
        end
        else a:=((LenX + 1) div 2);
    bmpLeft.Width:=a*wid + 1;
    bmpLeft.Height:=LenY*wid + 1;
    if (yy = 0) then begin
        bmpLeft.Canvas.pen.Color:=clWhite;
        bmpLeft.Canvas.Rectangle(0, 0, bmpLeft.Width, bmpLeft.Height);
        bmpLeft.Canvas.pen.Color:=clBlack;
    end;
    if (yy = 0)
        then begin
            yy1:=1;
            yy2:=LenY;
        end
        else begin
            yy1:=yy;
            yy2:=yy;
        end;
    for y:=yy1 to yy2 do begin
        if ((y mod 5) = 0) then d:=0 else d:=1;
        for x:=1 to a do bmpLeft.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid + 1, y*wid + d);
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
procedure TForm1.DrawTop(xx:integer);
var x, y, tx, ty, a, d, py, xx1, xx2:integer;
    tstr:string;
begin
    if (cbRjad.Checked)
        then begin
            d:=(bmpTop.Height - 1) div wid;
            a:=GetMaxCountRjadY;
            if (a <> d) then xx:=0;
        end
        else a:=((LenY + 1) div 2);
    bmpTop.Width:=LenX*wid + 1;
    bmpTop.Height:=a*wid + 1;
    if (xx = 0) then begin
        bmpTop.Canvas.pen.Color:=clWhite;
        bmpTop.Canvas.Rectangle(0, 0, bmpTop.Width, bmpTop.Height);
        bmpTop.Canvas.pen.Color:=clBlack;
    end;
    if (xx = 0)
        then begin
            xx1:=1;
            xx2:=LenX;
        end
        else begin
            xx1:=xx;
            xx2:=xx;
        end;
    for x:=xx1 to xx2 do begin
        if ((x mod 5) = 0) then d:=0 else d:=1;
        for y:=1 to a do bmpTop.Canvas.Rectangle((x - 1)*wid, (y - 1)*wid, x*wid + d, y*wid + 1);
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
procedure TForm1.RefreshPole;
begin
    DrawPole(Point(0, 0));
    if (not cbRjad.Checked) then DrawSmall;
    Buf.Canvas.Draw(bmpLeft.Width, bmpTop.Height, bmpPole);
    if (not cbRjad.Checked)
        then Buf.Canvas.Draw((bmpLeft.Width - bmpSmall.Width) div 2,
                             (bmpTop.Height - bmpSmall.Height) div 2, bmpSmall);
    pbPaint(Self);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.DrawPole(pt:TPoint);
var x, y, d1, d2, tx1, ty1, tx2, ty2:integer;
    pt1, pt2:TPoint;
begin
    bmpPole.Width:=LenX*wid + 1;
    bmpPole.Height:=LenY*wid + 1;

    if ((pt.x = 0) and (pt.y = 0)) then begin
        bmpPole.Canvas.Pen.Color:=clWhite;
        bmpPole.Canvas.Rectangle(0, 0, bmpPole.Width, bmpPole.Height);
        bmpPole.Canvas.Pen.Color:=clBlack;
    end;
    if (pt.x = 0)
        then begin
            pt1.x:=1;
            pt2.x:=LenX;
        end
        else begin
            pt1.x:=pt.x;
            pt2.x:=pt.x;
        end;
    if (pt.y = 0)
        then begin
            pt1.y:=1;
            pt2.y:=LenY;
        end
        else begin
            pt1.y:=pt.y;
            pt2.y:=pt.y;
        end;
    for x:=pt1.x to pt2.x do
        for y:=pt1.y to pt2.y do begin
            if ((x mod 5) = 0) then d1:=0 else d1:=1;
            if ((y mod 5) = 0) then d2:=0 else d2:=1;
            tx1:=(x - 1)*wid;
            ty1:=(y - 1)*wid;
            tx2:=x*wid;
            ty2:=y*wid;
            case (Data[x, y]) of
                0:  begin
                        bmpPole.Canvas.Brush.Color:=clWhite;
                        bmpPole.Canvas.Rectangle(tx1, ty1, tx2 + d1, ty2 + d2);
                    end;
                1:  begin
                        bmpPole.Canvas.Brush.Color:=clLtGray;
                        bmpPole.Canvas.Rectangle(tx1, ty1, tx2 + d1, ty2 + d2);
                        bmpPole.Canvas.Brush.Color:=clBlack;
                        bmpPole.Canvas.Ellipse(tx1 + 2, ty1 + 2, tx2 - 2, ty2 - 2);
                    end;
                2:  begin
                        bmpPole.Canvas.Brush.Color:=clLtGray;
                        bmpPole.Canvas.Rectangle(tx1, ty1, tx2 + d1, ty2 + d2);
                    end;
                3:  begin
                        bmpPole.Canvas.Brush.Color:=clRed;
                        bmpPole.Canvas.Rectangle(tx1, ty1, tx2 + d1, ty2 + d2);
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
    bChangeLen:=true;
    bUpDown:=false;
    LenX:=udCountX.Position;
    LenY:=udCountY.Position;
    bDown:=false;
    bPredpl:=false;

    ClearData;

    SetInfo(0, true, false, true, 0);

    od.InitialDir:=ExtractFileDir(Application.ExeName);
    sd.InitialDir:=od.InitialDir;
    spd.InitialDir:=od.InitialDir;

    Buf:=TBitMap.Create;
    Buf.PixelFormat:=pf4bit;
    Buf.Width:=10;
    Buf.Height:=10;

    bmpLeft:=TBitMap.Create;
    bmpLeft.PixelFormat:=pf4bit;
    bmpLeft.Width:=10;
    bmpLeft.Height:=10;
    bmpLeft.Canvas.Pen.Color:=clBlack;
    bmpLeft.Canvas.Brush.Color:=clWhite;
    bmpLeft.Canvas.Font.Name:='Times New Roman';
    bmpLeft.Canvas.Font.Style:=[fsBold];
    bmpLeft.Canvas.Font.Size:=fs;

    bmpTop:=TBitMap.Create;
    bmpTop.PixelFormat:=pf4bit;
    bmpTop.Width:=10;
    bmpTop.Height:=10;
    bmpTop.Canvas.Pen.Color:=bmpLeft.Canvas.Pen.Color;
    bmpTop.Canvas.Brush.Color:=bmpLeft.Canvas.Brush.Color;
    bmpTop.Canvas.Font.Name:=bmpLeft.Canvas.Font.Name;
    bmpTop.Canvas.Font.Style:=bmpLeft.Canvas.Font.Style;
    bmpTop.Canvas.Font.Size:=bmpLeft.Canvas.Font.Size;

    bmpPole:=TBitMap.Create;
    bmpPole.PixelFormat:=pf4bit;
    bmpPole.Width:=10;
    bmpPole.Height:=10;
    bmpPole.Canvas.Pen.Color:=clBlack;
    bmpPole.Canvas.Brush.Color:=clWhite;

    bmpSmall:=TBitMap.Create;
    bmpSmall.PixelFormat:=pf4bit;
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
    Shift:=Shift - [ssDouble]; // когда 2 раза нажимаеш возникает и это сообщение, а оно портачит
    if (edInput.Enabled) then edInput.SetFocus; // фокус едиту! :)
    if ((X < bmpLeft.Width) and (Y < bmpTop.Height)) then Exit; // если кликаем в области SmallBmp то выходим
    if (X < bmpLeft.Width) then begin // тут находится bmpLeft
        if (cbRjad.Checked) then Exit; // это пока не проработал...
        Y:=Y - bmpTop.Height; // получаем координаты начала bmpLeft
        X:=X;
        Y:=(Y div wid) + 1; // теперь номер ячейки
        X:=(X div wid);

        CurrPt.xy:=true; // записываем номер ячейки
        CurrPt.pt.y:=X;
        CurrPt.pt.x:=Y;

        if (Shift = [ssAlt, ssLeft]) then begin // если нужно расчитать этот ряд
            PrepRjadX(Y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad); // подготовка ряда
            Unit2.glLen:=LenX; // длинна
            if (not Unit2.Calculate) then begin // расчет - если не получился ...
                ShowMessage('Ошибка в кроссворде (строка ' + IntToStr(Y) + ').');
                RefreshPole; // обновляем поле
                Exit; // выходим
            end;
            for x:=1 to LenX do begin// тут обновляем ряд
                Form1.Data[x, y]:=Unit2.glData[x];
                Ver[x, y, 1]:=Unit2.glVer[x];
            end;
//            GetFin;
            RefreshPole; // и выводим его на екран
            Exit; // выходим
        end;
        {пересчет для координат для рядов}
        if (cbRjad.Checked)
            then X:=X - 1
            else X:=X - (((LenX + 1) div 2) - CountRjadX[Y]) + 1;
        
        if (Shift = [ssCtrl, ssLeft]) then begin // сдвиг рядов чисел
            for ty:=(Y + 1) to LenY do begin
                CountRjadX[ty - 1]:=CountRjadX[ty];
                for tx:=1 to CountRjadX[ty] do RjadX[tx, ty - 1]:=RjadX[tx, ty];
            end;
            CountRjadX[LenY]:=0;
        end;
        if (Shift = [ssCtrl, ssRight]) then begin // то же но в другую сторону
            for ty:=(LenY) downto Y do begin    // а возможен глюк
                CountRjadX[ty]:=CountRjadX[ty - 1];
                for tx:=1 to CountRjadX[ty] do RjadX[tx, ty]:=RjadX[tx, ty - 1];
            end;
            CountRjadX[Y]:=0;
        end;
        if (Shift = [ssLeft]) then begin // добавляем еще одну точку
            {получаем сумму всех точек, включая пустые промежутки из длинной в 1}
            j:=0;
            for tx:=1 to CountRjadX[Y] do
                j:=j + RjadX[tx, Y];
            if (X <= 0) then begin // добавление новой
                if ((j + CountRjadX[Y]) > (LenX - 1)) then Exit; // если выходим при добавлении за граници, то выходим
                for tx:=CountRjadX[Y] downto 1 do RjadX[tx + 1, Y]:=RjadX[tx, Y]; // смещаем все цифры ряда для добавления 1
                X:=1; // позиция добавления 
                inc(CountRjadX[Y]); // длинна ряда увеличилась
                RjadX[X, Y]:=0; // пока ноль...
            end;
            if ((j + CountRjadX[Y]) > LenX) then Exit; // если превышаем длинну - то выходим 
            RjadX[X, Y]:=RjadX[X, Y] + 1; // увеличиваем на 1
        end;
        {тут уменьшаем на 1}
        if (Shift = [ssRight]) then begin
            if (CountRjadX[Y] = 0) then Exit; // если ряд пуст то выходим
            if (X <= 0) then X:=1; // если нажали на пустую ячейку, то удалять будем первый
            RjadX[X, Y]:=RjadX[X, Y] - 1; // удаляем
            if (RjadX[X, Y] = 0) then begin // если там ноль получился
                if (X <> CountRjadX[Y]) then // и этот ноль не в конце  
                    for tx:=X to CountRjadX[Y] do // то сдвигаем
                        RjadX[tx, Y]:=RjadX[tx + 1, Y];
                dec(CountRjadX[Y]); // уменьшаем на 1 количество
            end;
        end;
        {тут прорисовка}
        if (cbMode.Checked) then begin
            DataFromRjadX(Y);
            DrawPole(Point(0, Y));
            DrawTop(0);
        end;
        if ((Shift = [ssCtrl, ssLeft]) or (Shift = [ssCtrl, ssRight]))
            then DrawLeft(0)
            else DrawLeft(Y);
        Draw(Point(-1, -1));
        Exit;
    end;
    // далее то же, но только с рядами в bmpTop
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
            Unit2.glLen:=LenY;
            if (not Unit2.Calculate) then begin
                ShowMessage('Ошибка в кроссворде (столбец ' + IntToStr(X) + ').');
                RefreshPole;
                Exit;
            end;
            for y:=1 to LenY do begin
                Form1.Data[x, y]:=Unit2.glData[y];
                Ver[x, y, 2]:=Unit2.glVer[y];
            end;
//            GetFin;
            RefreshPole;
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
                if (Y <> CountRjadY[X]) then
                    for ty:=Y to CountRjadY[X] do
                        RjadY[X, ty]:=RjadY[X, ty + 1];
                dec(CountRjadY[X]);
            end;
        end;

        if (cbMode.Checked) then begin
            DataFromRjadY(X);
            DrawPole(Point(X, 0));
            DrawLeft(0);
        end;
        if ((Shift = [ssCtrl, ssLeft]) or (Shift = [ssCtrl, ssRight]))
            then DrawTop(0)
            else DrawTop(X);
        Draw(Point(-1, -1));

        Exit;
    end;
    bDown:=true;
    pbMouseMove(Sender, Shift, X, Y); // а тут если на поле попали
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
    bChangeLen:=true;
    CurrPt.xy:=true;
    CurrPt.pt:=Point(1, 1);
    LenX:=udCountX.Position;
    LenY:=udCountY.Position;
    Draw(Point(0, 0));
    Exit; 
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
var bDraw:boolean;
begin
    Y:=Y - bmpTop.Height;
    X:=X - bmpLeft.Width;
    if ((X < 0) or (Y < 0)) then begin
        Label1.Caption:='';
        Exit;
    end;
    Y:=(Y div wid) + 1;
    X:=(X div wid) + 1;
    if ((Y <= 0) or (Y > LenY) or (X <= 0) or (X > LenX)) then begin
        Label1.Caption:='';
        Exit;
    end;
    Label1.Caption:=FloatToStr(Round(Ver[x, y, 1]*100)/100) + '     ' + FloatToStr(Round(Ver[x, y, 2]*100)/100);
    if (not bDown) then Exit;
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
            DrawLeft(Y);
            DrawTop(X);
        end;
        DrawPole(Point(X, Y));
        Draw(Point(-1, -1));
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
    DrawLeft(0);
    DrawTop(0);
    Draw(Point(-1, -1));
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TForm1.Check:TPoint;
var x, y, a1, a2:integer;
begin
    a1:=0;
    for x:=1 to LenX do
        for y:=1 to CountRjadY[x] do a1:=a1 + RjadY[x, y];
    a2:=0;
    for y:=1 to LenY do
        for x:=1 to CountRjadX[y] do a2:=a2 + RjadX[x, y];
    Result:=Point(a1, a2);  // разница рядов
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btCalcClick(Sender: TObject);
var x, y:integer;
    b, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, c:boolean; // b - произошли ли изменения, b2 - была ли ошибка, b3 - предполагали ли, b4 - после точки ошибки небыло, мы поставили пустоту для проверки, b5 - предполагать максимальной вероятности с учетом массива NoSet, b6 - если точка с максимальной вероятностью была найдена, b7 - последний прогон для нормального отображения вероятностей, b8 - если нажали остановить, b9 - если остановка по ошибке, b10 - предположение сработало, b11 - нудно для пропуска прогона по у если LenX больше LenY
    h, m, s, ms:word;
    MaxVer1, MaxVer2:Real;
    a1, a2:real;
    pt:TPoint;
begin
    if (btCalc.Tag = 0) // интерфейсные изменение Остановить-Расчет
        then begin
            btCalc.Caption:='&Стоп      ';
            btCalc.Tag:=1;
            udCountX.Enabled:=false;
            udCountY.Enabled:=false;
            cbMode.Enabled:=false;
            btSave.Enabled:=false;
            btLoad.Enabled:=false;
            cbLoadNaklad.Enabled:=true;
            cbVerEnable.Enabled:=false;
            btClear.Enabled:=false;
            btSaveBitmap.Enabled:=false;
            edInput.Enabled:=false;
        end
        else begin
            btCalc.Caption:='&Расчет    ';
            btCalc.Tag:=0;
            udCountX.Enabled:=true;
            udCountY.Enabled:=true;
            cbMode.Enabled:=true;
            btSave.Enabled:=true;
            btLoad.Enabled:=true;
            cbLoadNaklad.Enabled:=true;
            cbVerEnable.Enabled:=true;
            btClear.Enabled:=true;
            btSaveBitmap.Enabled:=true;
            edInput.Enabled:=true;
            edInput.SetFocus;
            SetInfo(0, true, false, false, 0);
            Exit; // сразу выходим
        end;

    t:=Now; //

    // проверка на совпадение рядов
    pt:=Check;
    if ((pt.x - pt.y) <> 0) then begin
        ShowMessage('Ошибка! Несовпадение на ' + IntToStr(x));
        btCalc.Click; // остановка
        Exit;
    end;
    //-----------------------------
    // сам рачсет
    for x:=1 to LenX do tChY[x]:=true;
    for y:=1 to LenY do tChX[y]:=true;
    for x:=1 to LenX do
        for y:=1 to LenY do
            Predpl.NoSet[x, y]:=false;
    b3:=false; // пока расчет идет без предположений
    b4:=false; //
    b5:=false;
    b7:=false;
    b8:=false;
    b9:=false;
    b10:=false;
    // для пропуска прогона по у если в группе x и чисел меньше (значения больше) и длинна строки (группы) меньше, это все для ускорения
    b11:=(LenX > LenY); // нужно для пропуска прогона
    a1:=0;
    for x:=1 to LenX do
        a1:=a1 + CountRjadY[x];
    a2:=0;
    for y:=1 to LenY do
        a2:=a2 + CountRjadX[y];
    b11:=(a1/LenY > a2/LenX);
//if (b11)
//    then CheckBox1.Caption:='t'
//    else CheckBox1.Caption:='f';
//b11:=CheckBox1.Checked;
    //----------------------------------------------------------
    bPredpl:=false;
    b:=false; // для b11
    Memo1.Clear;
    repeat
        if (b and b11) then b11:=false;
        b:=false;
        b2:=false;
        if ((not b5) and (not b11)) then begin // при поиску другой точки, или если LenX больше LenY (в начале) пропускаем этот шаг
            for y:=1 to LenY do begin
//                if (not bPredpl) then RefreshPole; // прорисовка поля
                SetInfo(y, true, bPredpl, false, Predpl.SetDot);
                Application.ProcessMessages;  // передышка
                b8:=(btCalc.Tag = 0);
                if (b8) then begin
                    if (bPredpl) then begin
                        LoadPustot;
                        bPredpl:=false;
                        RefreshPole; // прорисовка поля
                    end;
                    break;
                end;
                if (bPredpl)
                    then begin
                        if (Predpl.FinX[y]) then Continue;
                        if (not Predpl.ChX[y]) then Continue;
                    end
                    else begin
                        if (FinX[y]) then Continue;
                        if (not ChX[y]) then Continue;
                    end;
                PrepRjadX(y, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad); // подготовка строки
                Unit2.glLen:=LenX; // длинна строки
if (bPredpl)
then begin
    if (Predpl.SetDot = 1)
        then Memo1.Lines.Add('Ряд: ' + IntToStr(y) + ' предп. т')
        else Memo1.Lines.Add('Ряд: ' + IntToStr(y) + ' предп. п');
end
else Memo1.Lines.Add('Ряд: ' + IntToStr(y) + ' точно');
                if (not Unit2.Calculate) then begin // расчет ... если нет ни одной комбины - ошибка
                    if (not cbVerEnable.Checked) then begin
                        ShowMessage('Ошибка в кроссворде (строка ' + IntToStr(y) + ').');
                        b9:=true;
                        break;
                    end;
                    b:=false; // изменений нету
                    b2:=true; // ошибка была
                    break;
                end;
                for x:=1 to LenX do begin
//v                    if (bPredpl)
//v                        then Predpl.Ver[x, y, 1]:=Unit2.glVer[x]
//v                        else Ver[x, y, 1]:=Unit2.glVer[x];
                    Ver[x, y, 1]:=Unit2.glVer[x];
                    if (Data[x, y] <> Unit2.glData[x]) then begin
                        Data[x, y]:=Unit2.glData[x];
                        b:=true;
                        if (bPredpl)
                            then Predpl.ChY[x]:=true
                            else begin
                                ChY[x]:=true;
                                if (b10) then tChY[x]:=true;
                            end;
                    end;
                end;
                if (bPredpl)
                     then Predpl.ChX[y]:=false
                     else ChX[y]:=false;
            end;
            if (bPredpl) then GetFin;
            if (not bPredpl) then RefreshPole; // прорисовка поля
        end;
        if ((not bPredpl) and (not b9)) then b9:=GetFin;

        if ((not b2) and (not b5) and (not b8) and (not b9)) then begin // если была ошибка (b2) или надо найти другую точку (b5) или принудительно заканчиваем (b8) или была ошибка (b9) то пропускаем этот шаг
            // дальше то же только для столбцов
            for x:=1 to LenX do begin
//                if (not bPredpl) then RefreshPole; // прорисовка поля
                SetInfo(x, false, bPredpl, false, Predpl.SetDot);
                Application.ProcessMessages;
                b8:=(btCalc.Tag = 0);
                if (b8) then begin
                    if (bPredpl) then begin
                        LoadPustot;
                        bPredpl:=false;
                        RefreshPole; // прорисовка поля
                    end;
                    break;
                end;
                if (bPredpl)
                    then begin
                        if (Predpl.FinY[x]) then Continue;
                        if (not Predpl.ChY[x]) then Continue;
                    end
                    else begin
                        if (FinY[x]) then Continue;
                        if (not ChY[x]) then Continue;
                    end;
                PrepRjadY(x, Unit2.glData, Unit2.glRjad, Unit2.glCountRjad);
                Unit2.glLen:=LenY;
if (bPredpl)
then begin
    if (Predpl.SetDot = 1)
        then Memo1.Lines.Add('Ст.: ' + IntToStr(x) + ' предп. т')
        else Memo1.Lines.Add('Ст.: ' + IntToStr(x) + ' предп. п');
end
else Memo1.Lines.Add('Ст.: ' + IntToStr(x) + ' точно');
                if (not Unit2.Calculate) then begin
                    if (not cbVerEnable.Checked) then begin
                        ShowMessage('Ошибка в кроссворде (столбец ' + IntToStr(x) + ').');
                        b9:=true;
                        break;
                    end;
                    b:=false; // изменений нету
                    b2:=true; // ошибка была
                    break;
                end;
                c:=false;
                for y:=1 to LenY do begin
//v                    if (bPredpl)
//v                        then Predpl.Ver[x, y, 2]:=Unit2.glVer[y]
//v                        else Ver[x, y, 2]:=Unit2.glVer[y];
                    Ver[x, y, 2]:=Unit2.glVer[y];
                    if (Data[x, y] <> Unit2.glData[y]) then begin
                        b:=true;
                        Data[x, y]:=Unit2.glData[y];
                        if (bPredpl)
                            then Predpl.ChX[y]:=true
                            else begin
                                ChX[y]:=true;
                                if (b10) then tChX[y]:=true;
                            end;
                    end;
                end;
                if (bPredpl)
                     then Predpl.ChY[x]:=false
                     else ChY[x]:=false;
            end;
            if (bPredpl) then GetFin;
            if (not bPredpl) then RefreshPole;// прорисовка поля
            if (b11) then b:=true; // чтобы после прогона по х пошел прогон по у
        end;
        if ((not bPredpl) and (not b9)) then b9:=GetFin;

        if (b7 or b8) then b:=false; // все конец
        if ((cbVerEnable.Checked) and (not b) and (not b7) and (not b8) and (not b9)) then begin // если ничего не получается решить точно (b) и включено предположение (cbVerEnable.Checked) и последнего прогона нет (b7) и принудительно незавершали (b8) и ошибки нету
            b10:=false;
            if (b11) then b11:=false;
            if (b2) // ошибка была?
                then begin // была ошибка - отменяем все и ставим другую точку
                    b2:=false;
                    if (b3) // предполагали?
                        then begin // да
                            case (Predpl.SetDot) of // что предположили
                                1:  begin // после того как почставили точку ошибка вышла значит там пусто, что мы иделаем
                                        pt:=LoadPustot;
                                        SetPredplDot(false); // ставим пустоту
                                        b3:=true; // все еще предпологаем
                                        b:=true; // произошли измения
                                    end;
                                2:  begin // если хоть ставим хоть неставим точку все равно ошибка то ошибка в кроссворде
                                        if (b4) // проверка на вшивость, после того как поставили точку ошибки небыло, поставили пустоту появилась ошибка, значит тут точка стопудово
                                            then begin
                                                b4:=false;
                                                pt:=LoadPustot;
                                                bPredpl:=false;
                                                SetPredplDot(true); // ставим точку
                                                // очищаем сохранения пустых мест
Memo1.Lines.Add('Предп.т. ' + IntToStr(pt.y) + ', ' + IntToStr(pt.x));
                                                b3:=false; // закончили предположение
                                                b10:=true; // предположение сработало
                                                Draw(pt);
                                                b:=true; // произошли измения
                                            end
                                            else begin
                                                ShowMessage('Ошибка в кроссворде.');
                                                LoadPustot;
                                                bPredpl:=false;
                                                b9:=true;
                                            end;
                                    end;
                            end;
                        end
                        else begin // если была ошибка без предположений то в кросворде ошибка
                            ShowMessage('Ошибка в кроссворде.');
                            b9:=true;
                        end;
                end
                else begin // небыло ошибки - предполагаем
                    if (b3) // предполагали?
                        then begin // если уже предполагали то возвращаемся обратно и ставим в другом месте точку
                            case (Predpl.SetDot) of // что предположили
                                1:  begin // после того как поставили точку ошибки небыло, если будет ошибка после того как поставим пустое место, значит там точно точка.
                                        b4:=true; // отмечаем этот факт, чтобы потом можно было различить 2 ситуации: 1. ошибка после точки и пустоты, и проверку на вшивость
                                        pt:=LoadPustot;
                                        SetPredplDot(false); // ставим пустоту
                                        b3:=true; // все еще предполагаем предположение
                                        b:=true; // произошли измения
                                    end;
                                2:  begin
                                        if (b4)
                                            then begin // ни после точки ни после пустоты ошибок небыло
                                                b4:=false;
                                                pt:=LoadPustot;
                                                Predpl.NoSet[pt.x, pt.y]:=true;
                                                Draw(pt);
Memo1.Lines.Add('Галяк. ' + IntToStr(pt.y) + ', ' + IntToStr(pt.x));
                                                b5:=true;
                                                b3:=false; // закончили предположение
                                                b:=true; // произошли измения
                                            end
                                            else begin // после того как поставили точку была ошибка, а после пустоты небыло, значит там пустота стопудово
                                                // очищаем сохранения пустых мест
                                                pt:=Predpl.SetTo;
                                                bPredpl:=false;  // в этих двух строках порядок обязателен
                                                SetPredplDot(false); // в этих двух строках порядок обязателен
                                                Draw(pt);
Memo1.Lines.Add('Предп.п. ' + IntToStr(pt.y) + ', ' + IntToStr(pt.x));
                                                b3:=false; // закончили предположение
                                                b10:=true; // предположение сработало
                                                b:=true; // произошли измения
                                            end;
                                    end;
                            end;
                        end
                        else begin // если еще не предполпгпли
                            MaxVer1:=0; // пока вероятности такие
                            MaxVer2:=0;
                            b6:=false;
                            for x:=1 to LenX do  // по всему полю
                                for y:=1 to LenY do begin
                                    if ((MaxVer1 <= Ver[x, y, 1]) and (MaxVer2 <= Ver[x, y, 2]) and (Ver[x, y, 1] < 1) and (Ver[x, y, 2] < 1)) then begin // ищем наиболее вероятную точку, но не с вероятностью 1 и 0
                                        if (Predpl.NoSet[x, y]) then continue;
                                        MaxVer1:=Ver[x, y, 1];
                                        MaxVer2:=Ver[x, y, 2];
                                        pt:=Point(x, y);
                                        b6:=true;
                                    end;
                                end;
                            b6:=b6 and ((MaxVer1 > 0) or (MaxVer2 > 0)); // критерий отбора
                            if (b6) // нашли точку?
                                then begin // да
                                    if (not b5) then begin
                                        bPredpl:=true;
                                        SavePustot(Point(pt.x, pt.y)); // сохраняемся только если искали макс вероятность без учета массива NoSet
                                    end;
                                    Predpl.SetTo:=pt;
                                    Data[pt.x, pt.y]:=3;
                                    Draw(pt);
Memo1.Lines.Add('Предп. в ' + IntToStr(pt.y) + ', ' + IntToStr(pt.x));
                                    SetPredplDot(true); // ставим точку
                                    b:=true; // произошли изменения
                                    b3:=true; // предположили и поставили точку
                                end
                                else begin // нет
                                    if (b5) then begin
                                        LoadPustot; // возвращаем все как было
                                        bPredpl:=false;
                                        RefreshPole; //
                                    end;
                                    b:=true; // изменений нету
                                    for x:=1 to LenX do begin
                                        FinY[x]:=false;
                                        ChY[x]:=true;
                                    end;
                                    for y:=1 to LenY do begin
                                        FinX[y]:=false;
                                        ChX[y]:=true;
                                    end;
                                    b7:=true; // последний прогон для нормального отображения вероятностей
                                end;
                            b5:=false;
                        end;
                end;
        end;
        if (b9) then b:=false; // все конец
    until (not b);
    // очистка массивв флагов заполнености
    for x:=1 to LenX do begin
        ChY[x]:=true;
        FinY[x]:=false;
    end;
    for y:=1 to LenY do begin
        ChX[y]:=true;
        FinX[y]:=false;
    end;
    for x:=1 to LenX do
        for y:=1 to LenY do begin
            case (Data[x, y]) of
                1:  begin
                        Ver[x, y, 1]:=1;
                        Ver[x, y, 2]:=1;
                    end;
                2: begin
                        Ver[x, y, 1]:=0;
                        Ver[x, y, 2]:=0;
                    end;
            end;
        end;
    if (not b8) then btCalc.Click; // остановка
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.SetPredplDot(bDot: boolean);
var pt:TPoint;
begin
    pt:=Point(Predpl.SetTo.x, Predpl.SetTo.y);
    if (bDot)
        then begin
            Data[pt.x, pt.y]:=1;
            Predpl.SetDot:=1;
            // меняем вероятности
//v            if (bPredpl)
//v                then begin
//v                    Predpl.Ver[pt.x, pt.y, 1]:=1;
//v                    Predpl.Ver[pt.x, pt.y, 2]:=1;
//v                end
//v                else begin
                    Ver[pt.x, pt.y, 1]:=1;
                    Ver[pt.x, pt.y, 2]:=1;
//v                end;
        end
        else begin
            Data[pt.x, pt.y]:=2;
            Predpl.SetDot:=2;
            // меняем вероятности
//v            if (bPredpl)
//v                then begin
//v                    Predpl.Ver[pt.x, pt.y, 1]:=0;
//v                    Predpl.Ver[pt.x, pt.y, 2]:=0;
//v                end
//v                else begin
                    Ver[pt.x, pt.y, 1]:=0;
                    Ver[pt.x, pt.y, 2]:=0;
//v                end;
        end;
    // строка и солбец, содержащие эту точку пересчитать
    if (bPredpl)
        then begin
            Predpl.ChX[pt.y]:=true;
            Predpl.ChY[pt.x]:=true;
            Predpl.FinX[pt.y]:=false;
            Predpl.FinY[pt.x]:=false;
        end
        else begin
            ChX[pt.y]:=true;
            ChY[pt.x]:=true;
            FinX[pt.y]:=false;
            FinY[pt.x]:=false;
        end;
end;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.SavePustot(pt:TPoint);
var x, y:integer;
    b:boolean;
begin
    Predpl.bPredpl:=true;
    Predpl.SetDot:=1;
    for x:=1 to LenX do // по всему полю
        for y:=1 to LenY do begin
            b:=(Data[x, y] = 0); // пусто?
            Predpl.Pustot[x, y]:=b; // тут храняться все пустые точки
            if (b)
                then begin
                    if (tChY[x] and tChX[y]) then Predpl.NoSet[x, y]:=false; 
                end
                else Predpl.NoSet[x, y]:=true;
        end;
    for x:=1 to LenX do begin
        Predpl.ChY[x]:=ChY[x];
        Predpl.FinY[x]:=FinY[x];
        tChY[x]:=false;
    end;
    for y:=1 to LenY do begin
        Predpl.ChX[y]:=ChX[y];
        Predpl.FinX[y]:=FinX[y];
        tChX[y]:=false;
    end;
    Predpl.SetTo:=pt;
end;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TForm1.LoadPustot: TPoint;
var x, y:integer;
begin
    for x:=1 to LenX do
        for y:=1 to LenY do
            if (Predpl.Pustot[x, y]) then begin
                Data[x, y]:=0;
                ChX[y]:=true;
                ChY[x]:=true;
                FinY[x]:=false;
                FinX[y]:=false;
            end;
    Result:=Predpl.SetTo;
    Predpl.bPredpl:=false;
end;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
function TForm1.GetFin:boolean;
var c, c2:boolean;
    x, y:integer;
begin
    // заполнение поля
    c2:=true;
    for y:=1 to LenY do begin
        c:=false; // флаг закончености строки
        for x:=1 to LenX do  // по строке
            c:=c or (Data[x, y] = 0); // если заполнено
        c2:=c2 and (not c);
        if (bPredpl)  // массив флагов заполнености
            then Predpl.FinX[y]:=not c
            else FinX[y]:=not c;
    end;
    for x:=1 to LenX do begin
        c:=false; // флаг закончености строки
        for y:=1 to LenY do  // по строке
            c:=c or (Data[x, y] = 0); // если заполнено
        c2:=c2 and (not c);
        if (bPredpl) // массив флагов заполнености
            then Predpl.FinY[x]:=not c
            else FinY[x]:=not c;
    end;
    Result:=c2;
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
    for y:=1 to LenY do
        Data[y]:=Form1.Data[X, y];  // данные
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
        then sd.FilterIndex:=2
        else sd.FilterIndex:=1;
    case (sd.FilterIndex) of
        1:ext:='.jap';
        2:ext:='.jdt';
    end;
    if (sd.FileName <> '') then sd.FileName:=ChangeFileExt(sd.FileName, ext);
    if (not sd.Execute) then begin
        edInput.SetFocus;
        Exit; // запуск диалога
    end;
    tstr:=ExtractFileExt(sd.FileName); // расширение
    case (sd.FilterIndex) of
        1:ext:='.jap';
        2:ext:='.jdt';
    end;
    if (tstr <> ext) // если не те расширения ...
        then sd.FileName:=ChangeFileExt(sd.FileName, ext); //... то по умолчанию
    // грузим файл
    od.FileName:=sd.FileName;
    case (sd.FilterIndex) of
        1: SaveRjadToFile(sd.FileName);
        2: SaveDataToFile(sd.FileName);
    end;
    Form1.Caption:='Японские головоломки - ' + ExtractFileName(sd.FileName);
    edInput.SetFocus;    
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btLoadClick(Sender: TObject);
var tstr, tstr2, ext:string;
    b:boolean; // флаг накладывания
begin
    if (cbMode.Checked) // расширение по умолчанию (2 - файло редактора)
        then od.FilterIndex:=2
        else od.FilterIndex:=1;
    case (od.FilterIndex) of
        1:ext:='.jap';
        2:ext:='.jdt';
    end;
    if (od.FileName <> '') then od.FileName:=ChangeFileExt(od.FileName, ext);
    if (not od.Execute) then begin
        edInput.SetFocus;
        Exit; // запуск диалога
    end;
    tstr:=ExtractFileExt(od.FileName); // имя файла
    case (od.FilterIndex) of
        1:ext:='.jap';
        2:ext:='.jdt';
    end;
    if (tstr <> ext) // если не те расширения ...
        then od.FileName:=ChangeFileExt(od.FileName, ext); //... то по умолчанию
    if (not FileExists(od.FileName)) then begin
        edInput.SetFocus;
        Exit;
    end;
    sd.FileName:=od.FileName;
    cbRjad.Checked:=true;
    if (cbLoadNaklad.Checked) then begin
        tstr:=ChangeFileExt(od.FileName, '.jap');
        tstr2:=ChangeFileExt(od.FileName, '.jdt');
        if (FileExists(tstr) and FileExists(tstr2)) then begin
            cbMode.Checked:=false;
            cbMode.Caption:='Расш.';
            LoadRjadFromFile(tstr); // грузим файл
            LoadDataFromFile(tstr2);  // грузим файл
            bChangeLen:=true;
            bUpDown:=true;
            Draw(Point(0, 0));
            Form1.Caption:='Японские головоломки - ' + ExtractFileName(tstr) + ', ' + ExtractFileName(tstr2);
            SetInfo(0, true, false, true, 0);
            Exit;
        end;
    end;
    b:=(cbLoadNaklad.Checked and (cbMode.Checked xor (od.FilterIndex = 2)));
    case (od.FilterIndex) of
        1: begin // файл расшифровщика
            if (not b) then begin
                // перекл режим
                cbMode.Checked:=false;
                if (cbMode.Checked) then cbMode.Caption:='Редактор' else cbMode.Caption:='Расш.';
                ClearData; // очищаем поле
            end;
            LoadRjadFromFile(od.FileName); // грузим файл
            bChangeLen:=true;
            bUpDown:=true;
            Draw(Point(0, 0));
            Form1.Caption:='Японские головоломки - ' + ExtractFileName(od.FileName);
            SetInfo(0, true, false, false, 0);
            if (not b) then btCalc.Click;
        end;
        2: begin // файл редактора
            if (not b) then begin
                // перекл режим
                cbMode.Checked:=true;
                if (cbMode.Checked) then cbMode.Caption:='Редактор' else cbMode.Caption:='Расш.';
            end;
            LoadDataFromFile(od.FileName);  // грузим файл
            if (not b) then begin
                GetRjadX; // получаем ряды
                GetRjady;
            end;
            bChangeLen:=true;
            bUpDown:=true;
            Draw(Point(0, 0));
            Form1.Caption:='Японские головоломки - ' + ExtractFileName(od.FileName);
            SetInfo(0, true, false, true, 0);
        end;
    end;
    edInput.SetFocus;
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
    ClearData(cbMode.Checked); // очищаем поле
    CurrPt.xy:=true;
    CurrPt.pt:=Point(1, 1);
    Draw(Point(0, 0)); // прорисовка
    SetInfo(0, true, false, true, 0);
    edInput.SetFocus;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.edInputKeyPress(Sender: TObject; var Key: Char);
var i, j, a:integer;
    tstr:string;
begin
    if not (Key in ['.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', #13, #8]) then begin
        Key:=#0;
        Exit;
    end;
    if (Key = #13) then begin
        Key:=#0;
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
                    edInput.SelectAll;
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

        if (CurrPt.xy)
            then begin
                if (cbMode.Checked)
                    then Draw(Point(0, CurrPt.pt.x))
                    else begin
                        DrawLeft(CurrPt.pt.x);
                        Draw(Point(-1, -1));
                    end;
            end
            else begin
                if (cbMode.Checked)
                    then Draw(Point(CurrPt.pt.x, 0))
                    else begin
                        DrawTop(CurrPt.pt.x);
                        Draw(Point(-1, -1));
                    end;
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
        // делаем текст в едите выделенным 
        edInput.SelectAll;
//        edInput.Text:=''; // очищаем едит
    end;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.cbRjadClick(Sender: TObject);
begin
    Draw(Point(0, 0));
    edCountXChange(Self);
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btSaveBitmapClick(Sender: TObject);
var tstr:string;
begin
    if (not spd.Execute) then begin
        edInput.SetFocus;
        Exit;
    end;
    cbRjad.Checked:=true;
    tstr:=ExtractFileExt(spd.FileName); // расширение
    if (tstr = '') then spd.FileName:=spd.FileName + '.bmp'; // если нет разрешения то разрешение по умолчанию
    if (tstr <> '.bmp') then spd.FileName:=ChangeFileExt(spd.FileName, '.bmp'); //если не те расширения то по умолчанию
    Buf.SaveToFile(spd.FileName);
    cbRjad.Checked:=false;
    edInput.SetFocus;
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
procedure TForm1.Button1Click(Sender: TObject);
var RangeW:Word97.Range;
    v1, v2, v3:Variant;
    ov1, ov2:OleVariant;
    Row1:Word97.Row;
    i, y, x, k, ax, ay:integer;
    tstr:string;
begin
    WordApplication1.Connect; // запускаем ворд
    WordApplication1.Visible:=true; // невидимый
    WordDocument1.Activate;  // новый документ

    ay:=((LenY + 1) div 2);
    ax:=((LenX + 1) div 2);
    for y:=1 to (LenY + ay) do begin // по всем строкам
        tstr:='';
        if (y <= ay)
            then begin // ряды bmpTop
                for x:=1 to ax do tstr:=tstr + ' ' + #9;
                for x:=1 to LenX do begin
                    k:=y - ay + CountRjadY[x];
                    if (k > 0)
                        then tstr:=tstr + IntToStr(RjadY[x, k]) + #9
                        else tstr:=tstr + ' ' + #9;
                end;
            end
            else begin // ряды bmpLeft и само поле
                for x:=1 to (LenX + ax) do begin // по всей строке
                    if (x <= ax)
                        then begin // ряды bmpLeft
                            if (x <= (ax - CountRjadX[y - ay]))
                                then tstr:=tstr + ' ' + #9
                                else tstr:=tstr + IntToStr(RjadX[x - (ax - CountRjadX[y - ay]), y - ay]) + #9;
                        end
                        else begin // поле
                            tstr:=tstr + ' ' + #9;
                        end;
                end;
            end;
        tstr:=copy(tstr, 1, Length(tstr) - 1);
        WordDocument1.Range.InsertParagraphAfter;
        WordDocument1.Paragraphs.Last.Range.Font.Size:=6;
        WordDocument1.Paragraphs.Last.Range.Text:=tstr;
    end;

    RangeW:=WordDocument1.Content;
    v1:=RangeW;
    v1.ConvertToTable(#9, 19, LenX + (LenX + 1) div 2);
    Row1:=WordDocument1.Tables.Item(1).Rows.Get_First;
    Row1.Range.Font.Size:=1;
    Row1.Range.InsertParagraphAfter;
    ov1:=' ';
    Row1.ConvertToText(ov1);    

    v1:=WordDocument1.Tables.Item(1).Columns;
    for x:=1 to (ax + LenX) do
        v1.Item(x).Width:=11;

    v1:=WordDocument1.Tables.Item(1).Rows;
    for y:=1 to (ay + LenY) do
        v1.Item(y).Height:=11;

    v1:=WordDocument1.Tables.Item(1);
    for x:=(ax + 1) to (ax + LenX) do
        for y:=(ay + 1) to (ay + LenY) do
            if (Data[x - ax, y - ay] = 1)
                then v1.Cell(y, x).Shading.BackgroundPatternColor:=clDkGray;

    WordDocument1.Tables.Item(1).Range.Paragraphs.Format.SpaceBefore:=0;
    WordDocument1.Tables.Item(1).Range.Paragraphs.Format.SpaceAfter:=0;
    WordDocument1.Tables.Item(1).Range.Paragraphs.Format.FirstLineIndent:=0;

    v1:=WordDocument1.Tables.Item(1).Columns;
    for x:=(ax + 1) to (ax + LenX) do begin
        v1.Item(x).Borders.OutsideLineStyle:=1;

        v1.Item(x).Cells.VerticalAlignment:=wdCellAlignVerticalCenter;
    end;

    v1:=WordDocument1.Tables.Item(1).Rows;
    for y:=(ay + 1) to (ay + LenY) do begin
        v1.Item(y).Borders.OutsideLineStyle:=1;
        v1.Item(y).Cells.VerticalAlignment:=wdCellAlignVerticalCenter;
    end;

    v1:=WordDocument1.Tables.Item(1).Columns;
    for x:=ax to (ax + LenX) do
        if (((x - ax) mod 5) = 0)
            then v1.Item(x).Borders.Item(wdBorderRight).LineStyle:=7;

    v1:=WordDocument1.Tables.Item(1).Rows;
    for y:=ay to (ay + LenY) do
        if (((y - ay) mod 5) = 0)
            then v1.Item(y).Borders.Item(wdBorderBottom).LineStyle:=7;

    WordDocument1.Tables.Item(1).Columns.Borders.OutsideLineStyle:=7;

    WordApplication1.Visible:=true; // видимый
    WordApplication1.Disconnect; // отсоеденячемся от ворда
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.udCountXChangingEx(Sender: TObject; var AllowChange: Boolean; NewValue: Smallint; Direction: TUpDownDirection);
begin
    case Direction of
        updUp:bUpDown:=true;
        updDown:bUpDown:=false;
    end;
end;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.FormMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
begin
    Label1.Caption:='';
end;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.CheckBox1Click(Sender: TObject);
begin
    btClear.Click;
    btCalc.Click;
end;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.SetInfo(Rjad: integer; bRjadStolb, bPredpl, bTimeNow: boolean; bWhoPredpl:byte);
var x, y, a:integer;
    h, m, s, ms:word;
begin
    if (bTimeNow) then t:=Now;
    if (bPredpl)
        then begin
            if (bWhoPredpl = 1)
                then Label2.Caption:='Расчет: предп. т.'
                else Label2.Caption:='Расчет: предп. п.';
        end
        else Label2.Caption:='Расчет: точно';
    if (bRjadStolb)
        then Label4.Caption:=Format('Ряд: %d', [Rjad])
        else Label4.Caption:=Format('Столбец: %d', [Rjad]);
    DecodeTime(Now - t, h, m, s, ms);  //
    Label5.Caption:=Format('Время: %d-%d-%d', [(h*60) + m, s, ms]);
    if (bPredpl) then Exit;
    a:=0;
    for x:=1 to LenX do
        for y:=1 to LenY do
            if (Data[x, y] > 0) then a:=a + 1;
    Label3.Caption:='Открыто: ' + FloatToStr(Round(1000*a/(LenX*LenY))/10) + '%(' + IntToStr(a) + ')';
end;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
