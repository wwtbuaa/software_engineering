package com.wwt.warcraft;

import com.b3dgs.lionengine.core.Config;
import com.b3dgs.lionengine.engine.*;
/*����༸��ȫ���������������Ĺ��ܣ��ᴴ��һ�����ڣ�����ѡ��ֱ��ʵ���Ϸ���ԣ�
 * ����Chinese�Ƿ��������Ϸ֧������δ����д��֮ǰ����֪������Ϊδ����unicode
 * ���룬��ȷ���Ƿ�֧������
 * */
public class Options extends AbstractOptions{

	/**
	 *�������Ҫ��ö����ﶼҪ���������������䣬Ϊ�˲����������eclipse����ʾ��ɫ���ںܶ����ﶼ������һ�仰���ҾͲ�һһ˵���� 
	 */
	private static final long serialVersionUID = 1L;
	private static int[][] availableResolutions={{320, 200},{640, 400},{1280, 800}};
	private static int[] availableRates= {0, 120, 75, 60, 50, 30, 25, 10};
	private static String[] availableLanguages={"English","Chinese"};

	public Options(final Launcher launcher, Config arg1) {
		super(launcher, arg1, availableResolutions, availableRates, availableLanguages);
	}

}
