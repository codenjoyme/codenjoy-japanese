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
  object Label1: TLabel
    Left = 280
    Top = 48
    Width = 32
    Height = 13
    Caption = 'Label1'
  end
  object Label2: TLabel
    Left = 368
    Top = 48
    Width = 32
    Height = 13
    Caption = 'Label2'
  end
  object Label3: TLabel
    Left = 456
    Top = 48
    Width = 32
    Height = 13
    Caption = 'Label3'
  end
  object edCount: TEdit
    Left = 8
    Top = 8
    Width = 57
    Height = 21
    TabOrder = 0
    Text = '17'
    OnChange = edCountChange
  end
  object udCount: TUpDown
    Left = 65
    Top = 8
    Width = 15
    Height = 21
    Associate = edCount
    Min = 1
    Max = 40
    Position = 17
    TabOrder = 1
    Wrap = False
  end
  object btCalc: TButton
    Left = 96
    Top = 40
    Width = 75
    Height = 25
    Caption = '������'
    TabOrder = 2
    OnClick = btCalcClick
  end
  object Button1: TButton
    Left = 176
    Top = 40
    Width = 75
    Height = 25
    Caption = 'Button1'
    TabOrder = 3
    OnClick = Button1Click
  end
  object Button2: TButton
    Left = 512
    Top = 40
    Width = 75
    Height = 25
    Caption = 'Button2'
    TabOrder = 4
    OnClick = Button2Click    
  end
end
