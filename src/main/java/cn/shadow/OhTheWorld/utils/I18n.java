package cn.shadow.OhTheWorld.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.shadow.OhTheWorld.Main;

public class I18n {
	private static I18n instance = new I18n();
	public static I18n getInstance() { return instance; }
	
	public static boolean init() {
		File file = new File(Main.getInstance().getDataFolder(), "lang.json");
		if(!file.exists()) {
			instance = new I18n();
			String json = (new GsonBuilder().setPrettyPrinting().create()).toJson(instance);
			try {
				OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
		        oStreamWriter.append(json);
		        oStreamWriter.close();
		        return true;
			} catch (Exception ex) {
				LogUtil.getLogger().log(Level.SEVERE, instance.FailToCreateLang);
				ex.printStackTrace();
				return false;
			}
		} else {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
		        StringBuilder sb = new StringBuilder();
		        String line = reader.readLine();
		        while (line != null) {
		            sb.append(line);
		            line = reader.readLine();
		        }
		        instance = (new Gson()).fromJson(sb.toString(), I18n.class);
		        reader.close();
		        
		        String json = (new GsonBuilder().setPrettyPrinting().create()).toJson(instance);
				try {
					OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			        oStreamWriter.append(json);
			        oStreamWriter.close();
			        return true;
				} catch (Exception ex) {
					LogUtil.getLogger().log(Level.SEVERE, instance.FailToCreateLang);
					ex.printStackTrace();
					return false;
				}
			} catch (Exception ex) {
				LogUtil.getLogger().log(Level.SEVERE, instance.FailToLoadLang);
				ex.printStackTrace();
				return false;
			}
		}
	}
	
	public String FailToGetGenerator = "无法从插件%s中正确获取区块生成器，将忽略该生成器调用";
	public String FailToCreateWorld = "无法创建世界%s。控制台中可能有更多信息";
	public String FailToFixDimId = "无法正确锚定维度，Dim id=%s";
	public String FailToFixDimIdDueToWorldAlreadyLoaded = "无法正确锚定维度%s，因为该世界已被其他插件/mod先行加载";
	public String FailToGetWorldType = "获取可用世界类型失败";
	public String FailToGetWorldDimensionId = "无法正确获取维度id，世界：%s";
	public String FailToSetWorldType = "无法为指定的世界设定Forge地形生成器";
	public String FailToUnloadWorld = "无法卸载世界%s";
	public String FailToDeleteWorld = "无法删除世界%s";
	public String NotEnoughMoney = "你的余额不足";
	public String MainMenu = "主菜单";
	public String Status = "当前状态：";
	public String Enabled = "已激活";
	public String Disabled = "未激活";
	public String Back = "返回上一级";
	public String FailToSave = "配置文件保存失败";
	public String ProvidedByBukkit = "生成器由Bukkit提供";
	public String ProvidedByForge = "生成器由Forge Mod提供";
	public String DefaultGenerator = "默认生成器";
	public String ProvidedByVanilla = "生成器由MC原版提供";
	public String PreviousPage = "上一页";
	public String NextPage = "下一页";
	public String WorldAlreadyExist = "已存在同名世界";
	public String WorldCreateSuccess = "世界创建成功";
	public String WorldImportSuccess = "世界导入成功";
	public String TeleportTip = "某些mod世界的默认出生点可能并不好，您可以在某一位置用/setworldspawn命令将出生点重置到此处";
	public String BuildWorld = "开始构建世界";
	public String UpdateWorld = "开始更新世界";
	public String InexistWorld = "世界%s目录不存在，将忽略其加载";
	public String LeftClickToTeleport = "点击传送至该世界出生点";
	public String UnloadedWorld = "世界%s已从内存中卸载";
	public String UnableToUnloadWorld = "世界%s未能完成卸载";
	public String LoadedWorld = "世界%s已加载入内存";
	public String RemovedWorld = "世界%s已从配置文件中删除";
	public String NoPermissionInWorld = "你没有进入%s的权限";
	public String WorldNotFound = "世界文件夹%s不存在";
	public String WorldRegenSuccess = "世界%s已重新生成";
	public String WorldResetSuccess = "世界%s已重置";
	public String NotInMemory = "(未加载)";
	public String FailToCreateLang = "无法正确创建语言文件";
	public String FailToLoadLang = "无法正确加载语言文件";
	public String FailToInitConfig = "无法正确初始化世界配置";
	public String PlayerOnlyCommand = "该指令仅针对玩家有效";
	public String NoPermission = "无对应权限";
	public String BeforeCreatingWorld = "开始创建世界，将会有些许卡顿";
	
	public String LoreWorldEnv = "世界环境 : %s";
	public String LoreWorldDimId = "维度id : %s";
	public String LoreWorldAlias = "世界别名 : %s";
	
	public String WorldList[] = {
			"浏览世界配置",
	};
	
	public String TeleportWorldList = "世界传送列表";
	
	public String CreateNewWorld[] = {
			"创建新世界",
			"创建一个新的世界。玩家可以传送进入探索",
			"支持插件/Mods生成器"
	};
	
	public String ImportWorld[] = {
			"导入世界",
			"导入已存在的地图",
			"需先将地图文件夹放置在正确的位置"
	};
	
	public String WorldName[] = {
			"设置世界名称",
			"世界名称：“%s”",
			"点击设置世界名称",
			"不可与已有世界重复"
	};
	
	public String RespawnWorldName[] = {
			"设置重生世界名称",
			"世界名称：“%s”",
			"点击设置重生世界名称",
			"留空则采用MC原版重生机制"
	};
	
	public String WorldAlias[] = {
			"设置世界别名",
			"世界别名：“%s”",
			"点击设置世界的别名"
	};
	
	public String Seed[] = {
			"设置世界随机种子",
			"世界随机种子：“%s”",
			"点击设置世界的随机种子",
	};
	
	public String Regen[] = {
			"世界重生周期",
			"当前周期：%s小时（0为禁用该功能）",
			"点击设置世界重生周期",
			"到达世界后世界会自动以新的种子重新生成"
	};
	
	public String EnterFee[] = {
			"入场费用",
			"当前费用：%s",
			"需要前置Vault"
	};

	public String BedRespawn[] = {
			"在床上重生",
			"控制玩家是否可以在床上重生"
	};
	
	public String GameMode[] = {
			"游戏模式",
			"决定该世界的游戏模式"
	};
	
	public String Environment[] = {
			"环境设置",
			"决定该世界的环境渲染"
	};
	
	public String Difficulty[] = {
			"世界难度",
			"决定该世界的难度",
			"注: 对Forge无效"
	};
	
	public String GenerateStructures[] = {
			"结构生成",
			"决定要塞、地牢、神庙等结构是否生成"
	};
	
	public String KeepSpawnInMemory[] = {
			"出生点区块加载",
			"控制世界是否保持出生点区块加载"
	};
	
	public String BlueSky[] = {
			"保持蓝色天空",
			"避免y=61以下天空黑色渲染",
			"特别适合空岛世界",
			"需安装ProtocolLib",
			"注: 仅对玩家登入时所在的世界有效"
	};
	
	public String AllowNetherPortal[] = {
			"下界传送门",
			"是否允许玩家搭建下界传送门"
	};
	
	public String AllowEndPortal[] = {
			"末地传送门",
			"是否允许玩家激活末地传送门"
	};
	
	public String Hunger[] = {
			"是否开启饥饿",
			"是否开始MC自带的饥饿机制"
	};
	
	public String AutoHeal[] = {
			"是否开启自然回复",
			"是否开始MC自带的饱腹回血机制"
	};
	
	public String LockDimId[] = {
			"维度id锁定",
			"开启后，OhTheWorld将尝试在加载世界时复用之前的维度id",
			"可避免重启后Bukkit世界维度id改变的情况",
			"可改善与精致存储/传送石碑等mod的兼容性",
			"注1：仅支持Forge 1.12.2",
			"注2：仅对由OhTheWorld创建的世界有效"
	};
	
	public String WorldIcon[] = {
			"世界图标",
			"将显示在世界选择界面"
	};
	
	public String Weather[] = {
			"天气控制",
			"控制维度的天气变化"
	};
	
	public String Rain[] = {
			"下雨天气",
			"是否开启下雨天气"
	};
	
	public String Thunder[] = {
			"雷暴天气",
			"是否开启雷暴天气"
	};
	
	public String NormalAnimals[] = {
			"普通生物生成",
			"是否开启普通生物生成",
			"（只影响自然生成）"
	};
	
	public String WaterAnimals[] = {
			"水生生物生成",
			"是否开启水生生物生成",
			"（只影响自然生成）"
	};
	
	public String Monsters[] = {
			"敌对生物生成",
			"是否开启敌对生物生成",
			"（只影响自然生成）"
	};
	
	public String CreatureSpawn[] = {
			"生物生成设置",
			"控制各生物的生成"
	};
		
	public String WorldGenerator[] = {
			"世界生成器设置",
			"可选择生成插件/Mod的世界"
	};
	
	public String CustomGenerator[] = {
			"填入指定文本",
			"对于某些bukkit生成器（例如PlotSquare），需要指定额外参数",
			"单击此处将填入文本",
			"注：用户需先使用/otwg命令指定文本",
			"例如/otwg PlotSquared:size=64"
	};
	
	public String EmptyCustomGenerator = "请先用/otwg指令指定生成器文本";
	
	public String ClickToEdit = "单击编辑世界选项";
	
	public String EditWorld = "编辑世界设置";
	
	public String UnloadWorld[] = {
			"卸载世界",
			"仅从内存中卸载，你可以后续重新加载"
	};
	
	public String LoadWorld[] = {
			"加载世界",
			"根据配置文件内容加载该世界",
	};
	
	public String RegenWorld[] = {
			"重新生成世界",
			"使用新的随机种子重新生成该世界",
			"注：世界上的所有改动将丢失"
	};
	
	public String ResetWorld[] = {
			"重置世界",
			"用当前种子重新生成该世界",
			"注: 世界上的所有改动将丢失"
	};
	
	public String RemoveWorldConfig[] = {
			"卸载世界并删除配置",
			"从内存中卸载世界并删除世界的配置文件",
			"注: 不会删除世界的目录"
	};
}
