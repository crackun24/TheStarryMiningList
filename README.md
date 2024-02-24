# TheStarryMiningList

## 简介:

可以记录所有方块以及使用任何工具破坏方块的时候的挖掘数,并且显示在计分板上面。

## 配置文件:
路径位于"config/miningList.properties"中

__警告: 如果没有特殊的需求不要更改 ScoreboardDisplayName 选项,否则可能会造成计分板无法识别的错误__

<br>
ScoreboardDisplayName: 计分板在屏幕上显示的名字
<br>
ScoreboardName: 计分板的名字

```
默认的配置：
ScoreboardDisplayName = MiningList
ScoreboardName = MiningList
```

## 命令：

**/theStarryMiningList**
<br>用于控制计分板的显示/隐藏（默认关闭）。
<br>***权限: 仅op***
- 格式：/theStarryMiningListSwitch true/false
***
**/setScoreboardDisplayName**
<br>***权限: 仅op***
<br>用于设置计分板的**显示名称**，命令中的`scoreboardName`与`displayName`默认情况下都为`MiningList`,`scoreboardName`
是在配置文件中定义的，而`displayName`可以通过此命令在游戏中修改，也就是游戏内显示的名称。
- 格式：/setScoreboardDisplayName **[scoreboardName]** **[displayName]**
***
**/theStarryMiningDisplay**
<br>用于设置计分板的单个玩家是否显示(仅执行的玩家生效)
<br>***权限: 任意玩家***
- 格式：/theStarryMiningDisplay true/false
