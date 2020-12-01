object Form1: TForm1
  Left = 245
  Top = 144
  Width = 583
  Height = 266
  Caption = #1071#1087#1086#1085#1089#1082#1080#1077' '#1075#1086#1083#1086#1074#1086#1083#1086#1084#1082#1080
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  Position = poScreenCenter
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  PixelsPerInch = 96
  TextHeight = 13
  object Label1: TLabel
    Left = 9
    Top = 72
    Width = 6
    Height = 13
    Caption = '0'
  end
  object edCountX: TEdit
    Left = 4
    Top = 8
    Width = 44
    Height = 21
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    ReadOnly = True
    TabOrder = 1
    Text = '20'
    OnChange = edCountXChange
  end
  object udCountX: TUpDown
    Left = 48
    Top = 8
    Width = 12
    Height = 21
    Associate = edCountX
    Min = 1
    Max = 40
    Position = 20
    TabOrder = 2
    Wrap = False
    OnChangingEx = udCountXChangingEx
  end
  object btCalc: TButton
    Left = 6
    Top = 91
    Width = 73
    Height = 24
    Caption = '&'#1056#1072#1089#1095#1077#1090'    '
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 6
    OnClick = btCalcClick
  end
  object cbMode: TCheckBox
    Left = 8
    Top = 56
    Width = 67
    Height = 15
    Caption = #1056#1072#1089#1096'.'
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 5
    OnClick = cbModeClick
  end
  object edCountY: TEdit
    Left = 4
    Top = 32
    Width = 44
    Height = 21
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    ReadOnly = True
    TabOrder = 3
    Text = '20'
    OnChange = edCountXChange
  end
  object udCountY: TUpDown
    Left = 48
    Top = 32
    Width = 12
    Height = 21
    Associate = edCountY
    Min = 1
    Max = 40
    Position = 20
    TabOrder = 4
    Wrap = False
    OnChangingEx = udCountXChangingEx
  end
  object btSave: TButton
    Left = 6
    Top = 115
    Width = 73
    Height = 24
    Caption = '&'#1057#1086#1093#1088#1072#1085#1080#1090#1100
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 7
    OnClick = btSaveClick
  end
  object btLoad: TButton
    Left = 6
    Top = 139
    Width = 73
    Height = 24
    Caption = '&'#1047#1072#1075#1088#1091#1079#1080#1090#1100
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 8
    OnClick = btLoadClick
  end
  object btClear: TButton
    Left = 6
    Top = 163
    Width = 73
    Height = 24
    Caption = '&'#1054#1095#1080#1089#1090#1080#1090#1100
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 9
    OnClick = btClearClick
  end
  object edInput: TEdit
    Left = 6
    Top = 216
    Width = 65
    Height = 21
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 0
    OnKeyPress = edInputKeyPress
    OnMouseDown = edInputMouseDown
  end
  object cbRjad: TCheckBox
    Left = 64
    Top = 24
    Width = 14
    Height = 17
    Caption = 'cbRjad'
    TabOrder = 11
    OnClick = cbRjadClick
  end
  object btSaveBitmap: TButton
    Left = 6
    Top = 187
    Width = 73
    Height = 24
    Caption = #1056'&'#1080#1089#1091#1085#1086#1082
    TabOrder = 10
    OnClick = btSaveBitmapClick
  end
  object Button1: TButton
    Left = 32
    Top = 192
    Width = 63
    Height = 25
    Caption = 'Button1'
    TabOrder = 12
    Visible = False
    OnClick = Button1Click
  end
  object Panel1: TPanel
    Left = 88
    Top = 8
    Width = 481
    Height = 225
    BevelOuter = bvNone
    TabOrder = 13
    object pb: TPaintBox
      Left = 1
      Top = 1
      Width = 479
      Height = 223
      OnMouseDown = pbMouseDown
      OnMouseMove = pbMouseMove
      OnMouseUp = pbMouseUp
      OnPaint = pbPaint
    end
  end
  object cbVerEnable: TCheckBox
    Left = 61
    Top = 96
    Width = 13
    Height = 13
    TabOrder = 14
  end
  object od: TOpenDialog
    Filter = #1060#1072#1081#1083#1099' '#1088#1072#1089#1096#1080#1092#1088#1086#1074#1097#1080#1082#1072' (*.jap)|*.jap|'#1060#1072#1081#1083#1099' '#1088#1077#1076#1072#1082#1090#1086#1088#1072' (*.jdt)|*.jdt'
    Left = 80
    Top = 136
  end
  object sd: TSaveDialog
    Filter = #1060#1072#1081#1083#1099' '#1088#1072#1089#1096#1080#1092#1088#1086#1074#1097#1080#1082#1072' (*.jap)|*.jap|'#1060#1072#1081#1083#1099' '#1088#1077#1076#1072#1082#1090#1086#1088#1072' (*.jdt)|*.jdt'
    Left = 80
    Top = 112
  end
  object spd: TSavePictureDialog
    Filter = 'Bitmaps (*.bmp)|*.bmp'
    Left = 80
    Top = 184
  end
  object WordApplication1: TWordApplication
    AutoConnect = False
    ConnectKind = ckRunningOrNew
    AutoQuit = False
    Left = 96
    Top = 8
  end
  object WordDocument1: TWordDocument
    AutoConnect = False
    ConnectKind = ckRunningOrNew
    Left = 112
    Top = 16
  end
end
