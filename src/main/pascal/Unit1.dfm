object Form1: TForm1
  Left = 192
  Top = 103
  Width = 559
  Height = 424
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
    Left = 0
    Top = 0
    Width = 551
    Height = 397
    Align = alClient
    OnMouseDown = pbMouseDown
    OnPaint = pbPaint
  end
  object seSizeX: TSpinEdit
    Left = 448
    Top = 8
    Width = 81
    Height = 22
    MaxValue = 100
    MinValue = 1
    TabOrder = 0
    Value = 0
    OnChange = seSizeYChange
  end
  object seSizeY: TSpinEdit
    Left = 448
    Top = 32
    Width = 81
    Height = 22
    MaxValue = 100
    MinValue = 1
    TabOrder = 1
    Value = 0
    OnChange = seSizeYChange
  end
end
