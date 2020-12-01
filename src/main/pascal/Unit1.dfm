object Form1: TForm1
  Left = 69
  Top = 236
  Width = 876
  Height = 104
  Caption = 'Form1'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  PixelsPerInch = 96
  TextHeight = 13
  object pb: TPaintBox
    Left = 88
    Top = 8
    Width = 377
    Height = 25
    OnMouseDown = pbMouseDown
    OnMouseMove = pbMouseMove
    OnMouseUp = pbMouseUp
    OnPaint = pbPaint
  end
  object edCount: TEdit
    Left = 8
    Top = 8
    Width = 57
    Height = 21
    TabOrder = 0
    Text = '20'
    OnChange = edCountChange
  end
  object udCount: TUpDown
    Left = 65
    Top = 8
    Width = 12
    Height = 21
    Associate = edCount
    Min = 1
    Max = 40
    Position = 20
    TabOrder = 1
    Wrap = False
  end
  object btCalc: TButton
    Left = 88
    Top = 40
    Width = 75
    Height = 25
    Caption = '&Расчет'
    TabOrder = 2
    OnClick = btCalcClick
  end
  object cbMode: TCheckBox
    Left = 176
    Top = 44
    Width = 129
    Height = 17
    Caption = 'Редактор \ Расчет'
    TabOrder = 3
    OnClick = cbModeClick
  end
end
