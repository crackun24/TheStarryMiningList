# TheStarryMiningList

## 简介:

可以记录所有方块以及使用任何工具破坏方块的时候的挖掘数,并且显示在计分板上面。
<br>
同时还会记录玩家放置方块的次数（放置榜），服务器管理员可以在"挖掘榜"和"放置榜"之间随时切换。

## 配置文件:
路径位于"config/miningList.properties"中

__警告: 如果没有特殊的需求不要更改计分板名字相关的选项,否则可能会造成计分板无法识别的错误__

<br>
ScoreboardDisplayName: 挖掘榜在屏幕上显示的名字
<br>
ScoreboardName: 挖掘榜的名字
<br>
PlaceScoreboardDisplayName: 放置榜在屏幕上显示的名字
<br>
PlaceScoreboardName: 放置榜的名字
<br>
ScoreboardMode: 服务器启动时默认展示的榜单，可选 mining / placing

```
默认的配置：
ScoreboardDisplayName = MiningList
ScoreboardName = MiningList
PlaceScoreboardDisplayName = PlaceList
PlaceScoreboardName = PlaceList
ScoreboardMode = mining
```

## 命令：

**/miningboardg**
<br>用于控制计分板的显示/隐藏（默认显示）。
<br>***权限: 仅op***
- 格式：/miningboardg true/false

**/miningboardg mode**
<br>在挖掘榜和放置榜之间切换，切换结果会被保存，重启服务器后依然生效。
<br>***权限: 仅op***
- 格式：/miningboardg mode mining
- 格式：/miningboardg mode placing

**/miningboard**
<br>用于设置计分板的单个玩家是否显示(仅执行的玩家生效)
<br>***权限: 任意玩家***
- 格式：/miningboard true/false
