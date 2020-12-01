object Form1: TForm1
  Left = 245
  Top = 144
  Width = 619
  Height = 365
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
  OnMouseMove = FormMouseMove
  PixelsPerInch = 96
  TextHeight = 13
  object Label1: TLabel
    Left = 13
    Top = 72
    Width = 6
    Height = 13
    Caption = '0'
  end
  object edCountX: TEdit
    Left = 9
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
    Left = 53
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
    Left = 10
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
    Left = 12
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
    Left = 9
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
    Left = 53
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
    Left = 10
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
    Left = 10
    Top = 139
    Width = 73
    Height = 24
    Caption = '&'#1054#1090#1082#1088#1099#1090#1100'     '
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
    Left = 10
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
    Left = 10
    Top = 214
    Width = 73
    Height = 17
    AutoSize = False
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
    Left = 71
    Top = 23
    Width = 14
    Height = 17
    Caption = 'cbRjad'
    TabOrder = 11
    OnClick = cbRjadClick
  end
  object btSaveBitmap: TButton
    Left = 10
    Top = 187
    Width = 73
    Height = 24
    Caption = #1056'&'#1080#1089#1091#1085#1086#1082
    TabOrder = 10
    OnClick = btSaveBitmapClick
  end
  object Button1: TButton
    Left = 36
    Top = 200
    Width = 63
    Height = 25
    Caption = 'Button1'
    TabOrder = 12
    Visible = False
    OnClick = Button1Click
  end
  object Panel1: TPanel
    Left = 99
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
    Left = 65
    Top = 96
    Width = 13
    Height = 13
    TabOrder = 14
  end
  object cbLoadNaklad: TCheckBox
    Left = 65
    Top = 144
    Width = 13
    Height = 13
    TabOrder = 15
  end
  object gbInfo: TGroupBox
    Left = 4
    Top = 231
    Width = 93
    Height = 52
    TabOrder = 16
    object Label2: TLabel
      Left = 3
      Top = 7
      Width = 60
      Height = 11
      Caption = #1056#1072#1089#1095#1077#1090': '#1090#1086#1095#1085#1086
      Font.Charset = RUSSIAN_CHARSET
      Font.Color = clWindowText
      Font.Height = -9
      Font.Name = 'MS Serif'
      Font.Style = []
      ParentFont = False
    end
    object Label3: TLabel
      Left = 3
      Top = 17
      Width = 62
      Height = 11
      Caption = #1054#1090#1082#1088#1099#1090#1086': 100%'
      Font.Charset = RUSSIAN_CHARSET
      Font.Color = clWindowText
      Font.Height = -9
      Font.Name = 'MS Serif'
      Font.Style = []
      ParentFont = False
    end
    object Label4: TLabel
      Left = 3
      Top = 28
      Width = 28
      Height = 11
      Caption = #1056#1103#1076': 10'
      Font.Charset = RUSSIAN_CHARSET
      Font.Color = clWindowText
      Font.Height = -9
      Font.Name = 'MS Serif'
      Font.Style = []
      ParentFont = False
    end
    object Label5: TLabel
      Left = 4
      Top = 38
      Width = 30
      Height = 11
      Caption = #1042#1088#1077#1084#1103':'
      Font.Charset = RUSSIAN_CHARSET
      Font.Color = clWindowText
      Font.Height = -9
      Font.Name = 'MS Serif'
      Font.Style = []
      ParentFont = False
    end
  end
  object Memo1: TMemo
    Left = 4
    Top = 284
    Width = 93
    Height = 409
    Font.Charset = RUSSIAN_CHARSET
    Font.Color = clWindowText
    Font.Height = -9
    Font.Name = 'MS Serif'
    Font.Style = []
    ParentFont = False
    TabOrder = 17
    Visible = False
  end
  object RadioGroup1: TRadioGroup
    Left = 8
    Top = 288
    Width = 81
    Height = 49
    Caption = 'RadioGroup1'
    Items.Strings = (
      #1043#1083
      #1058#1095#1082
      #1055#1091#1089#1090)
    TabOrder = 18
    OnClick = RadioGroup1Click
  end
  object od: TOpenDialog
    Filter = #1060#1072#1081#1083#1099' '#1088#1072#1089#1096#1080#1092#1088#1086#1074#1097#1080#1082#1072' (*.jap)|*.jap|'#1060#1072#1081#1083#1099' '#1088#1077#1076#1072#1082#1090#1086#1088#1072' (*.jdt)|*.jdt'
    Left = 84
    Top = 136
  end
  object sd: TSaveDialog
    Filter = #1060#1072#1081#1083#1099' '#1088#1072#1089#1096#1080#1092#1088#1086#1074#1097#1080#1082#1072' (*.jap)|*.jap|'#1060#1072#1081#1083#1099' '#1088#1077#1076#1072#1082#1090#1086#1088#1072' (*.jdt)|*.jdt'
    Left = 84
    Top = 112
  end
  object spd: TSavePictureDialog
    Filter = 'Bitmaps (*.bmp)|*.bmp'
    Left = 84
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
