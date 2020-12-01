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
  TForm1 = class(TForm)
    edCount: TEdit;
    udCount: TUpDown;
    pb: TPaintBox;
    btClear: TButton;
    btCalc: TButton;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure pbMouseDown(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure pbPaint(Sender: TObject);
    procedure edCountChange(Sender: TObject);
    procedure pbMouseMove(Sender: TObject; Shift: TShiftState; X, Y: Integer);
    procedure pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
    procedure btCalcClick(Sender: TObject);
    procedure btClearClick(Sender: TObject);
  private
    PredCoord:TPoint;
    bDown:boolean;
    Buf, bmpLeft, bmpPole:TBitMap;
    Data:array [1..40] of TData;
    Rjad:array [1..40] of byte;
    CountRjad:integer;
    procedure Draw;
    procedure DrawPole;
    procedure DrawLeft;
    procedure GetRjad;
    procedure ClearData;
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
begin
    if (not bDown) then Exit;
    X:=X - bmpLeft.Width;
    X:=(X div wid) + 1;
    if (PredCoord.x = X) then Exit;
    if (ssLeft in Shift) then Data[X].Check:=1 else Data[X].Check:=0; 
    PredCoord:=Point(x, 1);
    GetRjad;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.pbMouseUp(Sender: TObject; Button: TMouseButton; Shift: TShiftState; X, Y: Integer);
begin
    bDown:=false;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btCalcClick(Sender: TObject);
begin
        
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
procedure TForm1.btClearClick(Sender: TObject);
var i:integer;
begin
    for i:=1 to udCount.Position do Data[i].Check:=0;
    Draw;
end;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
end.
