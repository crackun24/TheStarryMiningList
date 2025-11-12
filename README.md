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

**/miningboardg**
<br>用于控制计分板的显示/隐藏（默认关闭）。
<br>***权限: 仅op***
- 格式：/miningboardg true/false
***

**/miningboard**
<br>用于设置计分板的单个玩家是否显示(仅执行的玩家生效)
<br>***权限: 任意玩家***
- 格式：/miningboard true/false
