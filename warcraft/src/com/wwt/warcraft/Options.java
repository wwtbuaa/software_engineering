package com.wwt.warcraft;

import com.b3dgs.lionengine.core.Config;
import com.b3dgs.lionengine.engine.*;
/*这个类几乎全部是利用这个引擎的功能，会创建一个窗口，用于选择分辨率等游戏属性，
 * 关于Chinese是否在这个游戏支持在我未完整写完之前并不知道，因为未采用unicode
 * 编码，不确定是否支持中文
 * */
public class Options extends AbstractOptions{

	/**
	 *这个引擎要求好多类里都要加入下面的那条语句，为了不让这个类在eclipse里显示黄色，在很多类里都会有下一句话，我就不一一说明了 
	 */
	private static final long serialVersionUID = 1L;
	private static int[][] availableResolutions={{320, 200},{640, 400},{1280, 800}};
	private static int[] availableRates= {0, 120, 75, 60, 50, 30, 25, 10};
	private static String[] availableLanguages={"English","Chinese"};

	public Options(final Launcher launcher, Config arg1) {
		super(launcher, arg1, availableResolutions, availableRates, availableLanguages);
	}

}
