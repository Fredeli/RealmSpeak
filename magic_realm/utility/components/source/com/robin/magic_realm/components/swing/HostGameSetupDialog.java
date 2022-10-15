/* 
 * RealmSpeak is the Java application for playing the board game Magic Realm.
 * Copyright (c) 2005-2015 Robin Warren
 * E-mail: robin@dewkid.com
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 *
 * http://www.gnu.org/licenses/
 */
package com.robin.magic_realm.components.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.game.server.GameHost;
import com.robin.general.io.PreferenceManager;
import com.robin.general.swing.*;
import com.robin.magic_realm.components.utility.*;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;

public class HostGameSetupDialog extends AggressiveDialog {
	private static final String MAIN_TAB = "Game";
	private static final String EXTENDED_CHAR_RULES_TAB = "Character Capabilities";
	private static final String DENIZEN_RULES_TAB = "Denizen Rules";
	private static final String ADVANCED_MAGIC_RULES_TAB = "Advanced Magic";
	private static final String EXTENDING_GAME_SYSTEM_TAB = "Game System";
	private static final String EXPANDING_REALM_TAB = "Expanded Realm";
	private static final String REVISED_RULES_TAB = "Revised";
	private static final String OPTIONAL_CHAR_RULES_TAB = "Characters";
	private static final String FIRST_EDITION_RULES_TAB = "First Edition";
	private static final String HOUSE1_RULES_TAB = "Robin's House";
	private static final String HOUSE2_RULES_TAB = "Other House";
	private static final String HOUSE3_RULES_TAB = "More House";
	private static final String RANDOM_GEN_TAB = "# Generators";
	
	private static final Border TOP_LINE = BorderFactory.createMatteBorder(2,0,0,0,Color.blue);
	
	public static final GameVariant[] GAME_VERSION = {
		GameVariant.ORIGINAL_GAME_VARIANT,
		GameVariant.PRUITTS_GAME_VARIANT,
		GameVariant.EXP1_GAME_VARIANT,
	};
	
	private boolean loadingPrefs = false;
	private ControlNotifier notifier;
	protected OptionSetControl optionSetControl;
	
	protected GameData gameData;
	protected HostPrefWrapper hostPrefs;
	protected GameOptionPane optionPane;
	
	protected JTextField gamePort;
	protected JTextField hostName;
	protected JTextField hostEmail;
	protected JTextField smtpHost;
	protected JCheckBox emailNotification;
	protected JButton testEmailButton;
	protected JTextField gameTitle;
	protected JTextField gamePass;
	protected JRadioButton[] gameVariants;
	protected JLabel disableReqVpsWarning;
	
	protected JCheckBox anyVpsAllowedOption;
	protected JCheckBox fixedVps;
	
	protected Box timeLimitLine;
	protected Box vpAssignmentLine;
	protected IntegerField numberMonthsToPlay;
	protected IntegerField vpsToAchieve;
	
	protected VictoryConditionButton vpEndlessOption;
	protected VictoryConditionButton vpTimedOption;
	protected VictoryConditionButton vpSuddenDeathOption;
	protected VictoryConditionButton questBoqOption;
	protected VictoryConditionButton questQtrOption;
	protected VictoryConditionButton questGuildsOption;
	
	protected JCheckBox disableBattles;
	protected JLabel disableCombatWarning;
	protected JCheckBox disableSummoning;
	protected JLabel disableSummoningWarning;
	protected JCheckBox autosaveEnabled;
	protected JCheckBox alternativeTilesEnabled;
	protected JCheckBox mixExpansionTilesEnabled;
	protected JCheckBox includeExpansionSpells;
	protected JCheckBox includeNewSpells;
	protected JCheckBox includeNewSpells2;
	protected JCheckBox switchDaySpells;
	
	protected JCheckBox multiBoardEnabled;
	protected JSlider multiBoardCount;
	
	protected JRadioButton boardAutoSetup;
	protected JSlider minMapRating;
	protected JRadioButton boardPlayerSetup;
	
	protected JComboBox<GameObject> startingSeason;
	protected JCheckBox useWeather;
	protected JLabel optionalWeatherWarning;
	
	protected JButton defaultButton;
	
	public void loadPrefsFromData() {
		loadingPrefs = true;
		gamePort.setText(String.valueOf(hostPrefs.getGamePort()));
		hostName.setText(hostPrefs.getHostName());
		hostEmail.setText(hostPrefs.getHostEmail());
		smtpHost.setText(hostPrefs.getSmtpHost());
		emailNotification.setSelected(hostPrefs.isEmailNotifications());
		gameTitle.setText(hostPrefs.getGameTitle());
		gamePass.setText(hostPrefs.getGamePass());
		setSelectedGameVariant(hostPrefs.getGameKeyVals());
		numberMonthsToPlay.setText(String.valueOf(hostPrefs.getNumberMonthsToPlay()));
		fixedVps.setSelected(hostPrefs.isFixedVps());
		vpsToAchieve.setText(String.valueOf(hostPrefs.getVpsToAchieve()));
		disableBattles.setSelected(!hostPrefs.getEnableBattles());
		disableSummoning.setSelected(hostPrefs.getDisableSummoning());
		autosaveEnabled.setSelected(hostPrefs.getAutosaveEnabled());
		boardAutoSetup.setSelected(hostPrefs.getBoardAutoSetup());
		boardPlayerSetup.setSelected(hostPrefs.getBoardPlayerSetup());
		alternativeTilesEnabled.setSelected(hostPrefs.getAlternativeTilesEnabled());
		mixExpansionTilesEnabled.setSelected(hostPrefs.getMixExpansionTilesEnabled());
		includeExpansionSpells.setSelected(hostPrefs.getIncludeExpansionSpells());
		includeNewSpells.setSelected(hostPrefs.getIncludeNewSpells());
		includeNewSpells2.setSelected(hostPrefs.getIncludeNewSpells2());
		switchDaySpells.setSelected(hostPrefs.getSwitchDaySpells());
		multiBoardEnabled.setSelected(hostPrefs.getMultiBoardEnabled());
		multiBoardCount.setValue(hostPrefs.getMultiBoardCount());
		minMapRating.setValue(hostPrefs.getMinimumMapRating());
		vpTimedOption.setSelected(true); // this will be overridden by any other option
		vpEndlessOption.setSelected(hostPrefs.getRequiredVPsOff());
		questBoqOption.setSelected(hostPrefs.hasPref(Constants.QST_BOOK_OF_QUESTS));
		questQtrOption.setSelected(hostPrefs.hasPref(Constants.QST_QUEST_CARDS));
		questGuildsOption.setSelected(hostPrefs.hasPref(Constants.QST_GUILD_QUESTS));
		for (String key : optionPane.getGameOptionKeys()) {
			optionPane.setOption(key,hostPrefs.hasPref(key));
		}
		String season = hostPrefs.getStartingSeason();
		if (season == null) {
			startingSeason.setSelectedIndex(0);
		}
		else {
			GameObject startingSeasonGo = gameData.getGameObjectByName(hostPrefs.getStartingSeason());
			if (startingSeasonGo!=null) {
				startingSeason.setSelectedItem(startingSeasonGo);
			}
			else if (season.matches(RealmCalendar.RANDOM_SEASON)){
				startingSeason.setSelectedIndex(1);
			}
			else if (season.matches(RealmCalendar.UNPREDICTABLE_SEASON)){
				startingSeason.setSelectedIndex(2);
			}
		}
		useWeather.setSelected(hostPrefs.hasPref(Constants.OPT_WEATHER));
		vpSuddenDeathOption.setSelected(hostPrefs.hasPref(Constants.EXP_SUDDEN_DEATH));
		anyVpsAllowedOption.setSelected(hostPrefs.hasPref(Constants.HOUSE2_ANY_VPS));
		updateWarnings();
		loadingPrefs = false;
	}
	public void loadPrefsFromLocalConfiguration() {
		PreferenceManager prefMan = new PreferenceManager("RealmSpeak","host.cfg");
		if (prefMan.canLoad()) {
			prefMan.loadPreferences();
			loadPrefs(prefMan);
		}
	}
	public void savePrefsToLocalConfiguration() {
		PreferenceManager prefMan = new PreferenceManager("RealmSpeak","host.cfg");
		savePrefs(prefMan);
		prefMan.savePreferences();
	}
	protected void loadPrefs(PreferenceManager prefMan) {
		loadingPrefs = true;
		gamePort.setText(prefMan.get("gamePort"));
		hostName.setText(prefMan.get("hostName"));
		hostEmail.setText(prefMan.get("hostEmail"));
		smtpHost.setText(prefMan.get("smtpHost"));
		if (smtpHost.getText().trim().length()==0) {
			smtpHost.setText("smtp.yourdomain.com");
		}
		emailNotification.setSelected(prefMan.getBoolean("emailNotification"));
		gameTitle.setText(prefMan.get("gameTitle"));
		if (gameTitle.getText().trim().length()==0) {
			gameTitle.setText("RealmSpeak Online");
		}
		gamePass.setText(prefMan.get("gamePass"));
		setSelectedGameVariant(prefMan.get("gameVersion","Original Game"));
		numberMonthsToPlay.setText(prefMan.get("numberMonthsToPlay"));
		fixedVps.setSelected(prefMan.getBoolean("fixedVps"));
		vpsToAchieve.setText(prefMan.get("vpsToAchieve"));
		disableBattles.setSelected(!prefMan.getBoolean("battlesEnabled"));
		disableSummoning.setSelected(prefMan.getBoolean("summoningDisabled"));
		autosaveEnabled.setSelected(prefMan.getBoolean("autosaveEnabled"));
		boardAutoSetup.setSelected(prefMan.getBoolean("boardAutoSetup"));
		boardPlayerSetup.setSelected(prefMan.getBoolean("boardPlayerSetup"));
		alternativeTilesEnabled.setSelected(prefMan.getBoolean("alternativeTilesEnabled"));
		mixExpansionTilesEnabled.setSelected(prefMan.getBoolean("mixExpansionTilesEnabled"));
		
		includeExpansionSpells.setSelected(prefMan.getBoolean("includeExpansionSpells"));
		includeNewSpells.setSelected(prefMan.getBoolean("includeNewSpells"));
		includeNewSpells2.setSelected(prefMan.getBoolean("includeNewSpells2"));
		switchDaySpells.setSelected(prefMan.getBoolean("switchDaySpells"));
		
		multiBoardEnabled.setSelected(prefMan.getBoolean("multiBoardEnabled"));
		multiBoardCount.setValue(prefMan.getInt("multiBoardCount"));
		minMapRating.setValue(prefMan.getInt("minMapRating"));
		vpTimedOption.setSelected(true); // this will be overidden by any other option
		vpEndlessOption.setSelected(prefMan.getBoolean("disableReqVPs"));
		questBoqOption.setSelected(prefMan.getBoolean(Constants.QST_BOOK_OF_QUESTS));
		questQtrOption.setSelected(prefMan.getBoolean(Constants.QST_QUEST_CARDS));
		questGuildsOption.setSelected(prefMan.getBoolean(Constants.QST_GUILD_QUESTS));
		for (String key : optionPane.getGameOptionKeys()) {
			optionPane.setOption(key,prefMan.getBoolean(key));
		}
		String name = prefMan.get("startingSeason");
		if (name == null) {
			startingSeason.setSelectedIndex(0);
		}
		else {
			GameObject startingSeasonGo = gameData.getGameObjectByName(name);
			if (startingSeasonGo!=null) {
				startingSeason.setSelectedItem(startingSeasonGo);
			}
			else if(name.matches(RealmCalendar.RANDOM_SEASON)) {
				startingSeason.setSelectedIndex(1);
			}
			else if(name.matches(RealmCalendar.UNPREDICTABLE_SEASON)) {
				startingSeason.setSelectedIndex(2);
			}
		}
		useWeather.setSelected(prefMan.getBoolean(Constants.OPT_WEATHER));
		vpSuddenDeathOption.setSelected(prefMan.getBoolean(Constants.EXP_SUDDEN_DEATH));
		anyVpsAllowedOption.setSelected(prefMan.getBoolean(Constants.HOUSE2_ANY_VPS));
		updateWarnings();
		loadingPrefs = false;
	}
	protected void savePrefs(PreferenceManager prefMan) {
		prefMan.clear();
		prefMan.set("gamePort",gamePort.getText());
		prefMan.set("hostName",hostName.getText());
		prefMan.set("hostEmail",hostEmail.getText());
		prefMan.set("smtpHost",smtpHost.getText());
		prefMan.set("emailNotification",emailNotification.isSelected());
		prefMan.set("gameTitle",gameTitle.getText());
		prefMan.set("gamePass",gamePass.getText());
		prefMan.set("gameVersion",getSelectedGameVariant().getKeyVals());
		prefMan.set("numberMonthsToPlay",numberMonthsToPlay.getText());
		prefMan.set("fixedVps",fixedVps.isSelected());
		prefMan.set("vpsToAchieve",vpsToAchieve.getText());
		prefMan.set("battlesEnabled",!disableBattles.isSelected());
		prefMan.set("summoningDisabled",disableSummoning.isSelected());
		prefMan.set("autosaveEnabled",autosaveEnabled.isSelected());
		prefMan.set("boardAutoSetup",boardAutoSetup.isSelected());
		prefMan.set("boardPlayerSetup",boardPlayerSetup.isSelected());
		prefMan.set("alternativeTilesEnabled",alternativeTilesEnabled.isSelected());
		prefMan.set("mixExpansionTilesEnabled",mixExpansionTilesEnabled.isSelected());
		prefMan.set("includeExpansionSpells",includeExpansionSpells.isSelected());
		prefMan.set("includeNewSpells", includeNewSpells.isSelected());
		prefMan.set("includeNewSpells2", includeNewSpells2.isSelected());
		prefMan.set("switchDaySpells", switchDaySpells.isSelected());
		prefMan.set("multiBoardEnabled",multiBoardEnabled.isSelected());
		prefMan.set("multiBoardCount",multiBoardCount.getValue());
		prefMan.set("minMapRating",minMapRating.getValue());
		prefMan.set("disableReqVPs",vpEndlessOption.isSelected());
		prefMan.set(Constants.QST_BOOK_OF_QUESTS,questBoqOption.isSelected());
		prefMan.set(Constants.QST_QUEST_CARDS,questQtrOption.isSelected());
		prefMan.set(Constants.QST_GUILD_QUESTS,questGuildsOption.isSelected());
		
		for (String key : optionPane.getGameOptionKeys()) {
			prefMan.set(key,optionPane.getOption(key));
		}
		Object obj = startingSeason.getSelectedItem();
		if (obj instanceof GameObject) {
			GameObject go = (GameObject)startingSeason.getSelectedItem();
			prefMan.set("startingSeason",go.getName());
		}
		else {
			prefMan.set("startingSeason",obj.toString());
		}
		prefMan.set(Constants.OPT_WEATHER,useWeather.isSelected());
		prefMan.set(Constants.EXP_SUDDEN_DEATH,vpSuddenDeathOption.isSelected());
		prefMan.set(Constants.HOUSE2_ANY_VPS,anyVpsAllowedOption.isSelected());
	}
	protected void setSelectedGameVariant(String val) {
		for (int i=0;i<gameVariants.length;i++) {
			if (GAME_VERSION[i].getKeyVals().equals(val)) {
				gameVariants[i].setSelected(true);
				break;
			}
		}
	}
	private GameVariant getSelectedGameVariant() {
		GameVariant ret = null;
		for (int i=0;i<gameVariants.length;i++) {
			if (gameVariants[i].isSelected()) {
				ret = GAME_VERSION[i];
				break;
			}
		}
		return ret;
	}
	
	protected JButton startHost;
	protected JButton cancel;
	
	protected boolean didStart = false;
	protected boolean editMode;

	public HostGameSetupDialog(JFrame frame,String title,GameData data) {
		this(frame,title,data,true);
	}
	public HostGameSetupDialog(JFrame frame,String title,GameData data,boolean editMode) {
		super(frame,title,true);
		this.gameData = data;
		this.editMode = editMode;
		
		// Now, see if there isn't already a host preferences object
		hostPrefs = HostPrefWrapper.findHostPrefs(gameData);
		if (hostPrefs==null) { // This is NOT redundant, so don't delete it!
			GameObject gameObject = gameData.createNewObject();
			gameObject.setName("Host Preferences");
			hostPrefs = new HostPrefWrapper(gameObject);
		}
		
		initComponents();
		updateControls();
	}
	private void updateControls() {
		GameVariant variant = getSelectedGameVariant();
		gamePort.setEnabled(editMode);
		hostName.setEnabled(editMode);
		hostEmail.setEnabled(editMode);
		smtpHost.setEnabled(editMode);
		emailNotification.setEnabled(editMode);
		testEmailButton.setEnabled(editMode);
		gameTitle.setEnabled(editMode);
		gamePass.setEnabled(editMode);
		for (int i=0;i<gameVariants.length;i++) {
			gameVariants[i].setEnabled(editMode);
		}
		numberMonthsToPlay.setEnabled(editMode);
		fixedVps.setEnabled(editMode);
		vpsToAchieve.setEnabled(editMode);
		anyVpsAllowedOption.setEnabled(editMode);
		
		questGuildsOption.setEnabled(getSelectedGameVariant().getTitle()=="Expansion One");
		if (questGuildsOption.isSelected() && getSelectedGameVariant().getTitle()!="Expansion One") {
			questGuildsOption.setSelected(false);
		}
		
		if (!editMode) {
			vpEndlessOption.setEnabled(false);
			vpTimedOption.setEnabled(false);
			vpSuddenDeathOption.setEnabled(false);
			questBoqOption.setEnabled(false);
			questQtrOption.setEnabled(false);
			questGuildsOption.setEnabled(false);
		}
		
		disableBattles.setEnabled(editMode);
		disableSummoning.setEnabled(editMode);
		autosaveEnabled.setEnabled(editMode);
		boardAutoSetup.setEnabled(editMode);
		boardPlayerSetup.setEnabled(editMode);
		alternativeTilesEnabled.setEnabled(editMode);
		mixExpansionTilesEnabled.setEnabled(editMode && variant.getAllowBoardVariants());
		includeExpansionSpells.setEnabled(editMode && variant.getAllowBoardVariants());
		includeNewSpells.setEnabled(editMode);
		includeNewSpells2.setEnabled(editMode);
		switchDaySpells.setEnabled(editMode);
		multiBoardEnabled.setEnabled(editMode);
		multiBoardCount.setEnabled(editMode);
		minMapRating.setEnabled(editMode && boardAutoSetup.isSelected());
		defaultButton.setEnabled(editMode);
		startingSeason.setEnabled(editMode);
		useWeather.setEnabled(editMode && startingSeason.getSelectedIndex()>0);
	}
	private void initComponents() {
		setIconImage(IconFactory.findIcon("images/interface/options.gif").getImage());
		setSize(1048,750);
		setLocationRelativeTo(null);
		setModal(true);
		
		notifier = new ControlNotifier();
		notifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (!loadingPrefs) {
					madeChanges();
				}
			}
		});
		
		int hostOptionBoxWidth = 200;
		
		Box box;
		UniformLabelGroup group = new UniformLabelGroup();
		Box hostingBox = Box.createVerticalBox();
			box = group.createLabelLine("Host Player Name");
				hostName = notifier.getTextField();
				ComponentTools.lockComponentSize(hostName,hostOptionBoxWidth,25);
			box.add(hostName);
			box.add(Box.createHorizontalGlue());
		hostingBox.add(box);
			box = group.createLabelLine("Host e-mail");
				hostEmail = notifier.getTextField();
				ComponentTools.lockComponentSize(hostEmail,hostOptionBoxWidth,25);
			box.add(hostEmail);
				testEmailButton = notifier.getButton("Test Email");
				testEmailButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						doEmailTest();
					}
				});
			box.add(testEmailButton);
			box.add(Box.createHorizontalGlue());
		hostingBox.add(box);
			box = group.createLabelLine("SMTP Host");
				smtpHost = notifier.getTextField();
				ComponentTools.lockComponentSize(smtpHost,hostOptionBoxWidth,25);
			box.add(smtpHost);
				emailNotification = notifier.getCheckBox("e-mail notifications ON");
				emailNotification.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						updateWarnings();
					}
				});
			box.add(emailNotification);
			box.add(Box.createHorizontalGlue());
		hostingBox.add(box);
			box = group.createLabelLine("Game Title");
				gameTitle = notifier.getTextField();
				ComponentTools.lockComponentSize(gameTitle,hostOptionBoxWidth,25);
			box.add(gameTitle);
			box.add(new JLabel("  (this helps with email)"));
			box.add(Box.createHorizontalGlue());
		hostingBox.add(box);
			box = group.createLabelLine("Game Password");
				gamePass = notifier.getTextField();
				ComponentTools.lockComponentSize(gamePass,100,25);
			box.add(gamePass);
			box.add(Box.createHorizontalGlue());
		hostingBox.add(box);
			box = group.createLabelLine("Game Port");
				gamePort = notifier.getTextField();
				ComponentTools.lockComponentSize(gamePort,50,25);
			box.add(gamePort);
			box.add(Box.createHorizontalGlue());
		hostingBox.add(box);
			box = group.createLabelLine("Auto-save ON");
				autosaveEnabled = notifier.getCheckBox("");
			box.add(autosaveEnabled);
			box.add(Box.createHorizontalGlue());
		hostingBox.add(box);
		hostingBox.setBorder(BorderFactory.createTitledBorder("Hosting Options"));
		
		JPanel mainBox = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridy=0;
		c.weighty = 0.10;
		mainBox.add(hostingBox,c);
		
		JPanel boardSetupSplit = new JPanel(new BorderLayout());
		boardSetupSplit.add(buildGamePlayBox(group),"West");
		boardSetupSplit.add(buildBoardSetupBox(group),"Center");
		boardSetupSplit.add(buildRatingBox(),"East");
		boardSetupSplit.setMaximumSize(new Dimension(1000,100));
		c.gridy=1;
		c.weighty = 0.10;
		mainBox.add(boardSetupSplit,c);
		
		c.gridy=2;
		c.weighty = 1.00;
		c.ipady = 40;
		c.fill = GridBagConstraints.HORIZONTAL;
		mainBox.add(buildVictoryBox(),c);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(mainBox,"Center");
			
		optionPane = createOptionPane(editMode,mainPanel);
		
		optionPane.buildPane();
		
		setLayout(new BorderLayout());
		
		optionSetControl = new OptionSetControl(this);
		add(optionSetControl,"North");
		
		add(optionPane,"Center");
		
			box = Box.createHorizontalBox();
				defaultButton = new JButton("Use Defaults");
				defaultButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						setupDefaults();
						madeChanges();
					}
				});
			box.add(defaultButton);
			box.add(Box.createHorizontalGlue());
			if (editMode) {
					cancel = new JButton("Cancel");
					cancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							didStart=false;
							close();
						}
					});
				box.add(cancel);
			}
				startHost = new JButton(editMode?"Start":"Done");
				startHost.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						if (!editMode || captureHostPrefs()) {
							didStart = true;
							close();
						}
					}
				});
			box.add(startHost);
			box.add(Box.createHorizontalGlue());
			
		add(box,"South");
		
		getRootPane().setDefaultButton(startHost);		
		
		setupDefaults();
		updateWarnings();
	}
	private JPanel buildVictoryBox() {
		JPanel panel = new JPanel(new BorderLayout());
		
		Font font = new Font("Dialog",Font.BOLD,14);
		JPanel headerPanel = new JPanel(new GridLayout(2,1));
		JLabel vpLabel = new JLabel("VPs",SwingConstants.CENTER);
		vpLabel.setUI(new VerticalLabelUI(false));
		vpLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		vpLabel.setFont(font);
		vpLabel.setOpaque(true);
		vpLabel.setBackground(new Color(100,255,100));
		headerPanel.add(vpLabel);
		JLabel qLabel = new JLabel("Quests",SwingConstants.CENTER);
		qLabel.setUI(new VerticalLabelUI(false));
		qLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		qLabel.setFont(font);
		qLabel.setOpaque(true);
		qLabel.setBackground(new Color(255,100,255));
		headerPanel.add(qLabel);
		panel.add(headerPanel,BorderLayout.WEST);
		
		JPanel optionSpecifics = new JPanel(new GridLayout(1,3));
		anyVpsAllowedOption = notifier.getCheckBox("Allow players to choose any number of VPs ");
		anyVpsAllowedOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				updateWarnings();
			}
		});
		optionSpecifics.add(anyVpsAllowedOption);
		
		timeLimitLine = Box.createHorizontalBox();
		timeLimitLine.add(Box.createHorizontalStrut(30));
		timeLimitLine.add(new JLabel("# of Months:"));
		timeLimitLine.add(Box.createHorizontalStrut(5));
		numberMonthsToPlay = notifier.getIntegerField();
		ComponentTools.lockComponentSize(numberMonthsToPlay,50,20);
		timeLimitLine.add(numberMonthsToPlay);
		timeLimitLine.add(Box.createHorizontalGlue());
		fixedVps = notifier.getCheckBox("Custom amount of VPs ");
		fixedVps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				updateWarnings();
			}
		});
		timeLimitLine.add(fixedVps);
		timeLimitLine.add(Box.createHorizontalGlue());
		optionSpecifics.add(timeLimitLine);
		
		vpAssignmentLine = Box.createHorizontalBox();
		vpAssignmentLine.add(Box.createHorizontalStrut(30));
		vpAssignmentLine.add(new JLabel("VPs to Achieve:"));
		vpAssignmentLine.add(Box.createHorizontalStrut(5));
		vpsToAchieve = notifier.getIntegerField();
		ComponentTools.lockComponentSize(vpsToAchieve,50,20);
		vpAssignmentLine.add(vpsToAchieve);
		vpAssignmentLine.add(Box.createHorizontalGlue());
		optionSpecifics.add(vpAssignmentLine);
		
		panel.add(optionSpecifics,BorderLayout.NORTH);
	
		JPanel buttonPanel = new JPanel(new GridLayout(2,3));
		
		buttonPanel.add(vpEndlessOption=new VictoryConditionButton("Endless","No time limit or VPs\nNo Hall of Fame\nGame ends when you decide to quit"));
		buttonPanel.add(vpTimedOption=new VictoryConditionButton("Timed","Defined time limit\nStandard: Assign one VP per week plus one\nHighest score at game end wins"));
		buttonPanel.add(vpSuddenDeathOption=new VictoryConditionButton("Sudden Death","Predefined # of VPs\nNo Time Limit\nFirst to achieve VPs wins"));
		buttonPanel.add(questGuildsOption=new VictoryConditionButton("Guild Quests","Quests are given at guilds (expansion)\nQuests do not earn VPs\n\nENDLESS, TIMED or SUDDEN DEATH"));
		buttonPanel.add(questQtrOption=new VictoryConditionButton("Questing the Realm","Hand of Quest Cards\nFinish quests to earn VPs\n\nTIMED or SUDDEN DEATH"));
		buttonPanel.add(questBoqOption=new VictoryConditionButton("Book of Quests","Each character picks ONE quest\nFirst to finish quest wins\n\nSUDDEN DEATH only"));
		
		questGuildsOption.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
				questQtrOption.setSelected(false,false);
				questBoqOption.setSelected(false,false);
				
				vpEndlessOption.setEnabled(true);
				vpTimedOption.setEnabled(true);
				vpSuddenDeathOption.setEnabled(true);
				
				updateWarnings();
				
				if (!loadingPrefs) {
					madeChanges();
				}
			}
		});
		questQtrOption.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
				questGuildsOption.setSelected(false,false);
				questBoqOption.setSelected(false,false);
				
				vpEndlessOption.setEnabled(!questQtrOption.isSelected());
				vpTimedOption.setEnabled(true);
				vpSuddenDeathOption.setEnabled(true);
				
				updateWarnings();
				
				if (!loadingPrefs) {
					madeChanges();
				}
			}
		});
		questBoqOption.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
				questGuildsOption.setSelected(false,false);
				questQtrOption.setSelected(false,false);
				
				vpEndlessOption.setEnabled(!questBoqOption.isSelected());
				vpTimedOption.setEnabled(!questBoqOption.isSelected());
				vpSuddenDeathOption.setEnabled(true);
				
				updateWarnings();
				
				if (!loadingPrefs) {
					madeChanges();
				}
			}
		});

		
		vpEndlessOption.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
				vpEndlessOption.setSelected(true,false);
				vpTimedOption.setSelected(false,false);
				vpSuddenDeathOption.setSelected(false,false);
				
				questBoqOption.setEnabled(false);
				questQtrOption.setEnabled(false);
				
				updateWarnings();
				
				if (!loadingPrefs) {
					madeChanges();
				}
			}
		});
		vpTimedOption.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
				vpEndlessOption.setSelected(false,false);
				vpTimedOption.setSelected(true,false);
				vpSuddenDeathOption.setSelected(false,false);
				
				questBoqOption.setEnabled(false);
				questQtrOption.setEnabled(true);
				
				updateWarnings();
				
				if (!loadingPrefs) {
					madeChanges();
				}
			}
		});
		vpSuddenDeathOption.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
				vpEndlessOption.setSelected(false,false);
				vpTimedOption.setSelected(false,false);
				vpSuddenDeathOption.setSelected(true,false);
				
				questBoqOption.setEnabled(true);
				questQtrOption.setEnabled(true);
				
				updateWarnings();
				
				if (!loadingPrefs) {
					madeChanges();
				}
			}
		});
		
		
		panel.add(buttonPanel,BorderLayout.CENTER);
		
		panel.setBorder(BorderFactory.createTitledBorder("Victory Conditions"));
		return panel;
	}
	private Box buildRatingBox() {
		Box ratingBox = Box.createVerticalBox();
			minMapRating = notifier.getSlider(0,5,0);
			minMapRating.setSnapToTicks(true);
			minMapRating.setMajorTickSpacing(1);
			minMapRating.setPaintTicks(true);
			minMapRating.setPaintLabels(true);
			minMapRating.setOrientation(SwingConstants.VERTICAL);
			Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
			labelTable.put( 0, new JLabel("0 - Any (Fast)  ") );
			labelTable.put( 1, new JLabel("1") );
			labelTable.put( 2, new JLabel("2") );
			labelTable.put( 3, new JLabel("3") );
			labelTable.put( 4, new JLabel("4") );
			labelTable.put( 5, new JLabel("5 - Smart (Long)  ") );
			minMapRating.setLabelTable( labelTable );
		ratingBox.add(minMapRating);
		
		ratingBox.setBorder(BorderFactory.createTitledBorder("Minimum Map Rating"));
		return ratingBox;
	}
	private Box buildBoardSetupBox(UniformLabelGroup group) {
		Box boardSetupBox = Box.createVerticalBox();
			ButtonGroup boardSetupOptions = new ButtonGroup();
			Box box = group.createLabelLine("Generated Map");
				boardAutoSetup = notifier.getRadioButton("(RealmSpeak builds map)");
				boardSetupOptions.add(boardAutoSetup);
			box.add(boardAutoSetup);
				minMapRating = notifier.getSlider(0,5,0);
			box.add(Box.createHorizontalGlue());
			
		boardSetupBox.add(box);
			box = group.createLabelLine("Player Pick");
				boardPlayerSetup = notifier.getRadioButton("(Players build map)");
				boardSetupOptions.add(boardPlayerSetup);
			box.add(boardPlayerSetup);
			box.add(Box.createHorizontalGlue());
			
		boardSetupBox.add(box);
			box = group.createLabelLine("Alternative Tiles");
				alternativeTilesEnabled = notifier.getCheckBox("");
			box.add(alternativeTilesEnabled);
			box.add(Box.createHorizontalGlue());
			
		boardSetupBox.add(box);
			box = group.createLabelLine("Expansion Tiles");
				mixExpansionTilesEnabled = notifier.getCheckBox("(randomly mixed)");
			box.add(mixExpansionTilesEnabled);
			box.add(Box.createHorizontalGlue());
			
		boardSetupBox.add(box);
			box = group.createLabelLine("Expansion Spells");
				includeExpansionSpells = notifier.getCheckBox("");
			box.add(includeExpansionSpells);
			box.add(Box.createHorizontalGlue());
			
			boardSetupBox.add(box);
			box = group.createLabelLine("More Expansion Spells");
				includeNewSpells2 = notifier.getCheckBox("");
			box.add(includeNewSpells2);
			box.add(Box.createHorizontalGlue());
			
		boardSetupBox.add(box);
			box = group.createLabelLine("New Spells");
				includeNewSpells = notifier.getCheckBox("");
			box.add(includeNewSpells);
			box.add(Box.createHorizontalGlue());
			
		boardSetupBox.add(box);
			box = group.createLabelLine("Upgrade Day Spells");
				switchDaySpells = notifier.getCheckBox("");
			box.add(switchDaySpells);
			box.add(Box.createHorizontalGlue());				
			
		boardSetupBox.add(box);
			box = group.createLabelLine("Multiple Boards");
				multiBoardEnabled = notifier.getCheckBox("");
				multiBoardEnabled.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						updateWarnings();
					}
				});
			box.add(multiBoardEnabled);
				multiBoardCount = notifier.getSlider(2,4,2);
				multiBoardCount.setSnapToTicks(true);
				multiBoardCount.setMajorTickSpacing(1);
				multiBoardCount.setPaintTicks(true);
				multiBoardCount.setPaintLabels(true);
				ComponentTools.lockComponentSize(multiBoardCount,100,40);
			box.add(Box.createHorizontalStrut(20));
			box.add(multiBoardCount);
			box.add(Box.createHorizontalGlue());
		boardSetupBox.add(box);
		boardSetupBox.setBorder(BorderFactory.createTitledBorder("Board Setup Options"));
		return boardSetupBox;
	}
	private Box buildGamePlayBox(UniformLabelGroup group) {
		Box gamePlayBox = Box.createVerticalBox();
			Box box = group.createLabelLine("Game Variant");
				ButtonGroup variantGroup = new ButtonGroup();
				gameVariants = new JRadioButton[GAME_VERSION.length];
				for (int i=0;i<gameVariants.length;i++) {
					gameVariants[i] = notifier.getRadioButton(GAME_VERSION[i].toString(),i==0);
					gameVariants[i].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							updateControls();
						}
					});
					variantGroup.add(gameVariants[i]);
				}
			box.add(gameVariants[0]);
			box.add(Box.createHorizontalGlue());
		gamePlayBox.add(box);
			for (int i=1;i<gameVariants.length;i++) {
				box = group.createLine();
				box.add(gameVariants[i]);
				box.add(Box.createHorizontalGlue());
				gamePlayBox.add(box);
			}
			box = group.createLabelLine("Disable Combat");
				disableBattles = notifier.getCheckBox("");
				disableBattles.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						updateWarnings();
					}
				});
			box.add(disableBattles);
				disableCombatWarning = new JLabel("Combat is DISABLED!!");
				disableCombatWarning.setFont(new Font("Dialog",Font.BOLD,12));
				disableCombatWarning.setForeground(Color.red);
			box.add(disableCombatWarning);
			box.add(Box.createHorizontalGlue());
		gamePlayBox.add(box);
			box = group.createLabelLine("Disable Summoning");
				disableSummoning = notifier.getCheckBox("");
				disableSummoning.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						updateWarnings();
					}
				});
			box.add(disableSummoning);
				disableSummoningWarning = new JLabel("Summoning DISABLED!!");
				disableSummoningWarning.setFont(new Font("Dialog",Font.BOLD,12));
				disableSummoningWarning.setForeground(Color.red);
			box.add(disableSummoningWarning);
			box.add(Box.createHorizontalGlue());
		gamePlayBox.add(box);
			box = group.createLabelLine("Optional Season");
				ArrayList seasons = new ArrayList<>(RealmCalendar.findSeasons(gameData));
				seasons.add(1,RealmCalendar.RANDOM_SEASON);
				seasons.add(2,RealmCalendar.UNPREDICTABLE_SEASON);
				startingSeason = notifier.getComboBox(seasons.toArray());
				startingSeason.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						updateWarnings();
					}
				});
				startingSeason.setRenderer(new DefaultListCellRenderer() {
					public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
						super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
						if (index==3) {
							setBorder(TOP_LINE);
						}
						else {
							setBorder(null);
						}
						if (value instanceof GameObject) {
							GameObject go = (GameObject)value;
							String name = go.getThisAttribute("icon");
							ImageIcon icon = ImageCache.getIcon("season/"+name, 40);
							setIcon(icon);
							setText(go.getName());
						}
						else {
							setIcon(ImageCache.getIcon("tab/turn"));
							setText(value.toString());
						}
						return this;
					}
				});
				ComponentTools.lockComponentSize(startingSeason,160,40);
			box.add(startingSeason);
			box.add(Box.createHorizontalGlue());
		gamePlayBox.add(box);
			box = group.createLabelLine("Optional Weather");
				useWeather = notifier.getCheckBox("");
				useWeather.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						updateWarnings();
					}
				});
			box.add(useWeather);
				optionalWeatherWarning = new JLabel("Seasons without Weather!");
				optionalWeatherWarning.setFont(new Font("Dialog",Font.BOLD,12));
				optionalWeatherWarning.setForeground(Color.red);
			box.add(optionalWeatherWarning);
			box.add(Box.createHorizontalGlue());
		gamePlayBox.add(box);
		
		gamePlayBox.setBorder(BorderFactory.createTitledBorder("Game Play Options"));
		return gamePlayBox;
	}
	private void madeChanges() {
		optionSetControl.showNoSet();
	}
	public GameOptionPane getGameOptionPane() {
		return optionPane;
	}
	private GameOptionPane createOptionPane(boolean editMode,JPanel mainPanel) {
		// First tab is basic stuff
		GameOptionPane newOptionPane = new GameOptionPane(SwingConstants.LEFT,editMode);
		newOptionPane.addTab(MAIN_TAB,mainPanel);
		newOptionPane.addActionListener(notifier);
		
		String[] requiresNoVisitorMissionFlip = {Constants.HOUSE2_NO_MISSION_VISITOR_FLIPSIDE};
		String[] cannotIncludeIgnoreMissions = {Constants.HOUSE2_IGNORE_CAMPAIGNS};
		String[] overridesNoNegativePointsOrIgnoreCampaigns = {Constants.HOUSE1_NO_NEGATIVE_POINTS,Constants.HOUSE2_IGNORE_CAMPAIGNS};
		String[] cannotIncludeCampaignDebt = {Constants.HOUSE2_CAMPAIGN_DEBT};
		String[] exclusiveEnhancedMagicRules = {Constants.OPT_ENHANCED_MAGIC,Constants.HOUSE2_REVISED_ENHANCED_MAGIC};
		
		String[] requiresAlertedMonsterRule = {Constants.OPT_ALERTED_MONSTERS};
		String[] cannotIncludeSepRiderRules = {Constants.OPT_RIDING_HORSES};
		String[] requiresFumbleRules = {Constants.OPT_FUMBLE};
		String[] exclusiveMissileRules = {Constants.OPT_MISSILE,Constants.REV_MISSILE};
				
		String[] requiresDevelopmentRules = {Constants.EXP_DEVELOPMENT};
		String[] cannotIncludeExpDevRules = {Constants.EXP_DEVELOPMENT_PLUS,Constants.EXP_DEV_EXCLUDE_SW,Constants.EXP_DEV_3RD_REL};
		String[] requiresGrudges = {Constants.OPT_GRUDGES};
		String[] cannotIncludeExtGrudges = {Constants.TE_EXTENDED_GRUDGES};
		
		String[] exclusiveRandomGen = {Constants.RANDOM_R250_521,Constants.RANDOM_MERSENNE_TWISTER,Constants.RANDOM_ON_THE_FLY};
		
		newOptionPane.setTabHtmlDescription(EXTENDED_CHAR_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Extended Character Capabilities</font></body></html>");	
		newOptionPane.addOption(EXTENDED_CHAR_RULES_TAB,new GameOption(Constants.ADV_SERIOUS_WOUNDS,"SERIOUS WOUNDS A.1 - Characters suffer 1D6 wounds instead of death when harm matches vulnerability.",false));
		newOptionPane.addOption(EXTENDED_CHAR_RULES_TAB,new GameOption(Constants.ADV_AMBUSHES,"AMBUSHES A.2 - A successful HIDE roll on missile attacks while hidden, allow you to stay hidden.",false));
		newOptionPane.addOption(EXTENDED_CHAR_RULES_TAB,new GameOption(Constants.ADV_FLYING_ACTIVITIES,"FLYING ACTIVITIES A.3 - Flying characters can inspect the tiles they fly over and do the Alert activity.",false));
		newOptionPane.addOption(EXTENDED_CHAR_RULES_TAB,new GameOption(Constants.ADV_CACHING,"CACHING A.4 - Characters may record a CACHE action during their turn to create private caches of treasure and gold.",false));
		newOptionPane.addOption(EXTENDED_CHAR_RULES_TAB,new GameOption(Constants.ADV_DROPPING,"DROPPING AND LOSING BELONGINGS A.5 - With this rule, characters have a choice of dropping an item in plain sight (where anyone can find it), or throwing it away (so it can only be found by searching).",false));
		
		newOptionPane.setTabHtmlDescription(DENIZEN_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Denizen Optional Rules</font></body></html>");
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.OPT_QUIET_MONSTERS,"QUIET MONSTERS B.1.1-1.4 - Hidden characters and hirelings do not cause chits to flip, or monsters to be summoned.",false));
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.OPT_NO_BATTLE_DIST,"QUIET MONSTERS B.1.5 - When positioning attackers on a character sheet, they do NOT have to be positioned in as many red boxes as possible.",false));
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.OPT_ALERTED_MONSTERS,"ALERTED MONSTERS B.2 - When combat starts in a clearing with unhidden characters, hired or controlled denizens, then all uncontrolled denizens (not tremendous) turn dark side up.",false));
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.OPT_ALERTED_MONSTERS_VARIANT,"ALERTED MONSTERS B.2 Variant - When combat starts in a clearing  then all uncontrolled denizens (not tremendous) randomly turn dark side up.",false,null,requiresAlertedMonsterRule));
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.ADV_DRAGON_HEADS,"DRAGON HEADS B.3 - Instead of striking, Dragon Heads do a flame missile attack.",false));
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.TE_WATCHFUL_NATIVES,"WATCHFUL NATIVES B.4 - Unassigned natives become \"watchful\", and are not susceptible to suprise attacks.",false));
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.TE_EXTENDED_TREACHERY,"EXTENDED TREACHERY B.5 - Ordering a hired native to attack another member of his group now counts as treachery.",false));
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.OPT_GRUDGES,"GRUDGES/GRATITUDES B.6 - Selling/Buying items with special FAME prices affect friendliness with certain native groups.  Also, killing FRIENDLY or ALLIED natives affects your friendliness quite negatively.",false,null,null,cannotIncludeExtGrudges));
		newOptionPane.addOption(DENIZEN_RULES_TAB,new GameOption(Constants.TE_EXTENDED_GRUDGES,"EXTENDED GRUDGES B.7 - Killing an unhired neutral, unfriendly or enemy native reduces one's friendship level with the native's group one level for the rest of the game.  This penalty can only be applied once per character per native group per evening.  REQUIRES Grudges optional rule to be in play.",false,null,requiresGrudges));
		
		newOptionPane.setTabHtmlDescription(ADVANCED_MAGIC_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Advanced Magic Rules</font></body></html>");
		newOptionPane.addOption(ADVANCED_MAGIC_RULES_TAB,new GameOption(Constants.OPT_ENHANCED_MAGIC,"ENHANCED MAGIC C.1 - Spells and Magic chits are not tied up by the casting of a spell, which allows the casting of multiple instances of a single spell.",false,exclusiveEnhancedMagicRules,null));
		newOptionPane.addOption(ADVANCED_MAGIC_RULES_TAB,new GameOption(Constants.TE_BENEVOLENT_SPELLS,"BENEVOLENT SPELLS C.2 - With this rule, some spells can be cast on your hired natives without causing them to attack you.",false));
		newOptionPane.addOption(ADVANCED_MAGIC_RULES_TAB,new GameOption(Constants.OPT_ENHANCED_ARTIFACTS,"ENHANCED ARTIFACTS AND SPELL BOOKS C.3 - This rule allows characters to use Artifacts and Spell Books as extra Magic chits.",false));
		newOptionPane.addOption(ADVANCED_MAGIC_RULES_TAB,new GameOption(Constants.OPT_POWER_OF_THE_PIT_ATTACK,"POWER OF THE PIT OPTIONAL RULES - This rule requires the Power of the Pit attack to hit like an attack spell.",false));
		newOptionPane.addOption(ADVANCED_MAGIC_RULES_TAB,new GameOption(Constants.OPT_POWER_OF_THE_PIT_DEMON,"POWER OF THE PIT OPTIONAL RULES - Demon�s spell and characters' spells can cancel each other.",false));
		newOptionPane.addOption(ADVANCED_MAGIC_RULES_TAB,new GameOption(Constants.OPT_AUTOMATIC_ENCHANTING,"AUTOMATIC ENCHANTING C.5 - Certain map tiles flip over automatically at the end of each week, changing many of the roadways and making more color magic available.",false));
				
		newOptionPane.setTabHtmlDescription(EXTENDING_GAME_SYSTEM_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Extended Game System</font></body></html>");
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_COMMERCE,"COMMERCE D.2 - This includes changes in native trading prices based on the group and using the commerce table for selling goods.",false));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_FUMBLE,"FUMBLE D.3.1 - Roll on fumble table for melee attacks.",false,null,null,cannotIncludeSepRiderRules));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_RIDING_HORSES,"RIDING HORSES D.3.2-3.3 - Characters MUST play active horses to run away.  No MOVE chits or BOOTS cards are allowed.  Also, any character or denizen on horseback, can play both a maneuver for the rider, as well as the horse.  When attacking mounted opponents, the rider may be targeted separate from the horse, but are then subject to the rider's maneuver when calculating harm. (REQUIRES FUMBLE RULES)",false,null,requiresFumbleRules));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_STUMBLE,"STUMBLE D.3.4 - Roll on stumble table when running.",false));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_MISSILE,"OPTIONAL MISSILE TABLE D.3.4 - Use the Optional Missile Table for missile attacks.",false,exclusiveMissileRules,null));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_PENETRATING_ARMOR,"PENETRATING ARMOR D.3.5 - When a missile attack hits a character's armor, the harm is inflicted on the character but the armor reduces the harm.",false));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_TWO_HANDED_WEAPONS,"TWO HANDED WEAPONS (Book of Quests variant) - The use of a two-handed weapon limits a character�s ability to use a shield for defense.",false));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_DUAL_WIELDING,"DUAL WIELDING (Expansion 1) - Characters can use two weapons, one in each hand.",false));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_THROWING_WEAPONS,"THROWING WEAPONS (Expansion 1) - A character can throw corresponding weapons.",false));
		//newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_PARRY,"PARRYING (1st Ed. and Super Realm Project) - A character can parry target's attack with a weapon.",false));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_PARRY_LIKE_SHIELD,"PARRYING LIKE A SHIELD (Expansion 1) - A character can parry like with a shield other attacks with a weapon.",false));
		newOptionPane.addOption(EXTENDING_GAME_SYSTEM_TAB,new GameOption(Constants.OPT_SR_STEEL_AGAINST_MAGIC,"STEEL AGAINST MAGIC (Super Realm Project) - A character with no active armor counters casting a spell may also use his staff to make a separate attack.",false));
		
		newOptionPane.setTabHtmlDescription(EXPANDING_REALM_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Expanding the Realm</font></body></html>");
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_CUSTOM_CHARS,"CUSTOM CHARACTERS - Allow players to choose from custom characters.",true,null,requiresDevelopmentRules));
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_NO_DWELLING_START,"SOLITAIRE E.1.5-1.6 - No Dwelling Start - Dwellings/Ghosts are NOT revealed at the start, rather discovered like the Campfires.  Since there are no Dwellings at the start, you must enter the map from the edge.",false));
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_DOUBLE_MONSTER_DIE,"SOLITAIRE E.1.7 - Two Rows Prowling - Rather than roll a single die for prowling monsters, two dice are rolled, and two rows are prowling at once.  If the die rolls are the same, only one row prowls.",false));
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_DEVELOPMENT,"DEVELOPMENT E.5 - Characters may start at a level less than four, and work their way up by earning chits.",false,null,null,cannotIncludeExpDevRules));
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_DEVELOPMENT_PLUS,"EXTENDED DEVELOPMENT E.6 - Characters may develop past level four.  No chits are gained past four, but each new level leads to a reward, as outlined in the 3rd edition rules.",false,null,requiresDevelopmentRules));
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_MONSTER_DIE_PER_SET,"COMBINDING REALMS E.7 - Separate monster die for each Magic Realm set.",false));
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_DEV_EXCLUDE_SW,"EXCLUDE STARTING WORTH - For purposes of development ONLY, starting equipment value is not considered when gaining chits but is considered for end game victory points.",false,null,requiresDevelopmentRules));
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_DEV_3RD_REL,"GAIN 3RD RELATIONSHIPS - When you achieve 3rd level, you receive the relationships you would have normally had.  If you are playing with GRUDGES/GRATITUDES, these levels ARE affected by the difference.",false,null,requiresDevelopmentRules));
		newOptionPane.addOption(EXPANDING_REALM_TAB,new GameOption(Constants.EXP_BOUNTY_POINTS_FOR_DISCOVERIES,"BOUNTY POINTS FOR DISCOVERIES (Book of Quests Variant) - A character gains points for recording certain discoveries.",false));
		
		newOptionPane.setTabHtmlDescription(OPTIONAL_CHAR_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Optional Character Rules</font></body></html>");
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.TE_KNIGHT_ADJUSTMENT,"KNIGHTS ADJUSTMENT (3rd ed) - To limit the Black/White Knight's advantage, their ALLY starts off as FRIENDLY.",false));
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.OPT_CHAR_ABILITY_WIZARD_MAGIC_CHIT,"1.2a - WIZARD uses a MAGIC III2* instead of one MOVE M5 chit",false));
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.OPT_CHAR_ABILITY_WIZARD_IGNORES_SPX,"1.2b - WIZARD ignores SPX",false));
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.OPT_CHAR_ABILITY_CAPTAIN,"1.3 - CAPTAIN rolls one die for all HIRE rolls",false));
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.OPT_CHAR_ABILITY_WOODSGIRL,"1.4 - WOODSGIRL uses one die for all HIDE, SEARCH, and MEETING rolls in any Woods Clearing (not just tiles)",false));
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.OPT_CHAR_ABILITY_MAGICIAN,"1.5 - MAGICIAN's alerted MAGIC chits don't fatigue at Midnight (become active again)",false));
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.OPT_CHAR_ABILITY_DRUID_SUMMON,"1.6a - DRUID doesn't summon monsters from Site chits.",false));
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.OPT_CHAR_ABILITY_DRUID_CURSES,"1.6b - DRUID is immune to curses.",false));
		newOptionPane.addOption(OPTIONAL_CHAR_RULES_TAB,new GameOption(Constants.OPT_CHAR_ABILITY_ELF,"1.7 - ELF must choose between Light Elf, or Great Elf.  Affects which chits can be played during the game.",false));
		
		newOptionPane.setTabHtmlDescription(REVISED_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Revised Rules</font></body></html>");
		newOptionPane.addOption(REVISED_RULES_TAB,new GameOption(Constants.REV_MISSILE,"Revised Missile Table - Use the Revised Optional Missile Table for all missile attacks.",false,exclusiveMissileRules,null));
		newOptionPane.addOption(REVISED_RULES_TAB,new GameOption(Constants.REV_DAMPEN_FAST_SPELLS,"Hamblen's Kludge Adjustment - Drop one sharpness star from Attack spells (ie., Fiery Blast) cast at speed zero.",false));
		
		newOptionPane.setTabHtmlDescription(FIRST_EDITION_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">First Edition Rules</font></body></html>");
		newOptionPane.addOption(FIRST_EDITION_RULES_TAB,new GameOption(Constants.FE_AMBUSH_END_OF_COMBATROUND,"AMBUSH ROLL AT THE END OF COMBAT ROUND - The ambush roll is made at the end of a combat round.", false));
		newOptionPane.addOption(FIRST_EDITION_RULES_TAB,new GameOption(Constants.FE_SEARCH_TABLES,"FIRST EDITION SEARCH TABLES - Search tables of first edition for Search, Peer and Locate.", false));
		newOptionPane.addOption(FIRST_EDITION_RULES_TAB,new GameOption(Constants.FE_KILLER_CAVES,"KILLER CAVES - Horses are killed when they enter a cave clearing.", false));
		newOptionPane.addOption(FIRST_EDITION_RULES_TAB,new GameOption(Constants.FE_DEADLY_REALM,"DEADLY REALM - Unhired natives, medium and heavy monsters are turned darker side up at the start of the game (and at the start of each month). Lost City and Lost Castle summon monsters like sound chits. Characters must assign attackers equally to the combat boxes.", false));
		newOptionPane.addOption(FIRST_EDITION_RULES_TAB,new GameOption(Constants.FE_STEEL_AGAINST_MAGIC,"STEEL AGAINST MAGIC - A character cannot play a MAGIC counter, if he has any weapon counter except a staff activated.", false));
		
		newOptionPane.setTabHtmlDescription(HOUSE1_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Robin's House Rules</font></body></html>");
		newOptionPane.addOption(HOUSE1_RULES_TAB,new GameOption(Constants.HOUSE1_DWARF_ACTION,"Productive Dwarf - (This rule replaces section 1.1 of the advantage section for the Dwarf)  The Dwarf must spend one additional consecutive move phase to enter any non-cave clearing, but otherwise receives the normal number of phases.  (Special thanks to Daniel Farrow for this alternative rule which is much more workable than the one I was using!)",false));
		newOptionPane.addOption(HOUSE1_RULES_TAB,new GameOption(Constants.HOUSE1_CHIT_REMAIN_FACE_UP,"Persistent Chits - Once sound/warning chits have been discovered, they remain face up for the remainder of the game.",false));
		newOptionPane.addOption(HOUSE1_RULES_TAB,new GameOption(Constants.HOUSE1_NO_NEGATIVE_POINTS,"Positive Only Points - Fame and Notoriety are not allowed to go negative.  Any meeting result that requires more Fame/Notoriety than you have is considered Block/Battle.",false,cannotIncludeCampaignDebt,null));
		newOptionPane.addOption(HOUSE1_RULES_TAB,new GameOption(Constants.HOUSE1_DONT_RECYCLE_CHARACTERS,"No Character Recycle - Once a character dies, they cannot be played again for the remainder of the game.",false));
		newOptionPane.addOption(HOUSE1_RULES_TAB,new GameOption(Constants.HOUSE1_ALLOW_BIRDSONG_REARRANGE,"Birdsong Rearrangment - The rules for Magic Realm specify that you cannot rearrange your belongings during Birdsong.  This house rule relaxes that restriction (good for new players).",false));
		newOptionPane.addOption(HOUSE1_RULES_TAB,new GameOption(Constants.HOUSE1_FORCE_INN_AFTER_GAMESTART,"Force Inn After Start - Characters joining AFTER the start of the game, MUST start at the Inn, regardless of their normal options.",false));
		newOptionPane.addOption(HOUSE1_RULES_TAB,new GameOption(Constants.HOUSE1_NO_SECRETS,"No Secrets - When you are looting, or searching, the results of your finds are reported in the log for everyone to see.",false));
		newOptionPane.addOption(HOUSE1_RULES_TAB,new GameOption(Constants.HOUSE1_ALLOW_LEVEL_GAINS_PAST_FOUR,"Level Rewards Past Four - When playing extended development rules, you will get rewards past level four if you advance during the game.",false));
		
		newOptionPane.setTabHtmlDescription(HOUSE2_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Other House Rules</font></body></html>");
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_NO_SPELL_LIMIT,"No Spell Limit - Magic Realm limits you to 14 spells per character.  This option removes that limit.",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_MONSTER_WEAPON_NOFLIP,"No Flip with Monster Weapon Hit - Tremendous monsters with a weapon (Head or Club) will only flip when their body hits.",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_DECLINE_OPPORTUNITY,"Decline Opportunity - When you roll an OPPORTUNITY on a meeting table, you will be presented with an option to reroll on the next friendlier table OR take the next result down on the existing table (a two).",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_RED_SPECIAL_SHELTER,"Extra Shelters - The Lost City and Lost Castle chits count as shelters, for purposes of sheltered phases and weather.",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_REVISED_ENHANCED_MAGIC,"Revised Enhanced Magic - Spells are not tied up by the casting of a spell, which allows the casting of multiple instances of a single spell.  Unlike normal Enhanced Magic, MAGIC chits ARE tied up by each spell.",false,exclusiveEnhancedMagicRules,null));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_NATIVES_REMEMBER_DISCOVERIES,"Natives Remember Discoveries - When native leaders become unhired, they do not lose their recorded discoveries.  Of course, if they are killed, they are lost regardless.",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_NO_MISSION_VISITOR_FLIPSIDE,"No Mission/Visitor Flip - Has the effect of making all sides of these chits behave like individual chits, so you can use both sides of the chit at the same time.  This also prevents the chits from flipping when a 6 is rolled on day 7.",false,null,null,cannotIncludeIgnoreMissions));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_IGNORE_CAMPAIGNS,"No Campaigns - Exclude campaign chits when doing initial setup.  This rule requires the previous rule.",false,cannotIncludeCampaignDebt,requiresNoVisitorMissionFlip));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_CAMPAIGN_DEBT,"Campaign Debt - Allows characters pick up campaign chit, even if it means they will have negative points.",false,overridesNoNegativePointsOrIgnoreCampaigns,null));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_DAY_END_TRADING_ON,"Day End Trading ON - Day End Trading will be ON by default for all new characters.  This can still be turned off by individual characters, if desired.",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_NO_NATIVES_BATTLING,"No natives battling - Unhired natives don't participate in combat.",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_NATIVES_FRIENDLY,"No attacking of friendly natives (Book of Learning) - FRIENDLY and ALLIED natives cannot be lured or targeted.",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_PEACE_WITH_NATURE_SITES,"Peace with Nature ability extends to site chits - When character with this ability ends his turn, the site chits in his tile do not summon monsters from the Chart of Appearances.",false));
		newOptionPane.addOption(HOUSE2_RULES_TAB,new GameOption(Constants.HOUSE2_MULTIPLE_SUMMONING,"Multiple Summoning - Allows chits to summon many times in a single round.",false));
		
		newOptionPane.setTabHtmlDescription(HOUSE3_RULES_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">More House Rules</font></body></html>");
		newOptionPane.addOption(HOUSE3_RULES_TAB,new GameOption(Constants.HOUSE3_DWELLING_ARMOR_REPAIR,"Armor Repair - Repair armor at any dwelling during a trade phase, for the difference between the intact price, and the damaged price.",false));
		newOptionPane.addOption(HOUSE3_RULES_TAB,new GameOption(Constants.HOUSE3_SNOW_HIDE_EXCLUDE_CAVES,"Snow Hiding in Caves - Ignore special weather conditions that prevent hiding due to snow or soft ground, when you perform the HIDE activity in a cave.",false));
		newOptionPane.addOption(HOUSE3_RULES_TAB,new GameOption(Constants.HOUSE3_NO_VP_DEVELOPMENT_RAMP,"No VP Development Ramp - When playing with development rules, earn one chit for every VP gained, regardless of level",false));
		newOptionPane.addOption(HOUSE3_RULES_TAB,new GameOption(Constants.HOUSE3_NO_RESTRICT_VPS_FOR_DEV,"No VP Restrictions for Development - For purposes of development only, don't restrict VP gains to assigned VPs.",false));
		newOptionPane.addOption(HOUSE3_RULES_TAB,new GameOption(Constants.HOUSE3_SHOW_DISCARED_QUEST,"Show discarded quest cards - When discarding a quest card, the name of the quest is shown in the log.",false));
		
		newOptionPane.setTabHtmlDescription(RANDOM_GEN_TAB,"<html><body><font face=\"Helvetica, Arial, sans-serif\">Random Number Generator Preference - For more details see:</font><br>http://www.qbrundage.com/michaelb/pubs/essays/random_number_generation.html</body></html>");
		newOptionPane.addOption(RANDOM_GEN_TAB,new GameOption(Constants.RANDOM_R250_521,"Use R250/521 - A very fast shift-register sequence random number generator, invented by Kirkpatrick and Stoll in 1981.",false,exclusiveRandomGen,null));
		newOptionPane.addOption(RANDOM_GEN_TAB,new GameOption(Constants.RANDOM_MERSENNE_TWISTER,"Use Mersenne Twister - A twisted GFSR(624,397) invented by Matsumora and Nishimura in 1996.",false,exclusiveRandomGen,null));
		newOptionPane.addOption(RANDOM_GEN_TAB,new GameOption(Constants.RANDOM_ON_THE_FLY,"Use Java Generator - Generating different results, even when loading same game and doing identical actions.",false,exclusiveRandomGen,null));
		
		
		return newOptionPane;
	}
	private void updateWarnings() {
		disableCombatWarning.setVisible(disableBattles.isSelected());
		disableSummoningWarning.setVisible(disableSummoning.isSelected());
		optionalWeatherWarning.setVisible(!useWeather.isSelected() && startingSeason.getSelectedIndex()>0);
		useWeather.setEnabled(startingSeason.getSelectedIndex()>0);
		multiBoardCount.setEnabled(multiBoardEnabled.isSelected());
		multiBoardCount.setVisible(multiBoardEnabled.isSelected());
		minMapRating.setEnabled(boardAutoSetup.isSelected());
		smtpHost.setEnabled(emailNotification.isSelected());
		testEmailButton.setEnabled(emailNotification.isSelected());
		
		timeLimitLine.setVisible(vpTimedOption.isSelected());
		vpAssignmentLine.setVisible((vpSuddenDeathOption.isSelected() || (vpTimedOption.isSelected() && fixedVps.isSelected() && !anyVpsAllowedOption.isSelected())) && !questBoqOption.isSelected());
		anyVpsAllowedOption.setVisible((vpTimedOption.isSelected() || vpSuddenDeathOption.isSelected()) && !questBoqOption.isSelected());
		
		updateControls();
	}
	private void setupDefaults() {
		loadingPrefs = true;
		gamePort.setText(String.valueOf(GameHost.DEFAULT_PORT));
		hostName.setText("Game Master");
		hostEmail.setText("");
		smtpHost.setText("smtp.yourdomain.com");
		emailNotification.setSelected(false);
		gameTitle.setText("RealmSpeak Online");
		gamePass.setText("pass");
		gameVariants[0].setSelected(true);
		numberMonthsToPlay.setText("1");
		disableBattles.setSelected(false);
		autosaveEnabled.setSelected(true);
		boardAutoSetup.setSelected(true);
		alternativeTilesEnabled.setSelected(false);
		mixExpansionTilesEnabled.setSelected(false);
		includeExpansionSpells.setSelected(false);
		includeNewSpells.setSelected(false);
		includeNewSpells2.setSelected(false);
		switchDaySpells.setSelected(false);
		multiBoardEnabled.setSelected(false);
		multiBoardCount.setValue(2);
		minMapRating.setValue(0);
		vpEndlessOption.setSelected(false);
		questBoqOption.setSelected(false);
		questQtrOption.setSelected(false);
		questGuildsOption.setSelected(false);
		anyVpsAllowedOption.setSelected(false);
		startingSeason.setSelectedIndex(0);
		useWeather.setSelected(true);
		vpTimedOption.setSelected(true);
		fixedVps.setSelected(false);
		vpsToAchieve.setText("5");
		
		for (String key : optionPane.getGameOptionKeys()) {
			optionPane.setOption(key,false);
		}
		
		updateWarnings();
		loadingPrefs = false;
	}
	public static int readInt(String val) {
		try {
			Integer n = Integer.valueOf(val);
			return n.intValue();
		}
		catch(NumberFormatException ex) {
			// ignore
		}
		return -1;
	}
	public boolean captureHostPrefs () {
		// Before doing anything, validate the fields
		
		// No empty fields allowed
		if (gamePort.getText().length()==0 ||
				hostName.getText().length()==0 ||
				gameTitle.getText().length()==0 ||
				gamePass.getText().length()==0) {
			JOptionPane.showMessageDialog(null,"You must enter a value in every field");
			return false;
		}
		
		// Some fields require a number
		if (readInt(gamePort.getText())<1000) {
			JOptionPane.showMessageDialog(null,"Game Port must be greater than 1000");
			return false;
		}
		else if (vpTimedOption.isSelected() && readInt(numberMonthsToPlay.getText())<1) {
			JOptionPane.showMessageDialog(null,"Number of months to play must be a number greater than zero");
			return false;
		}
		else if ((vpSuddenDeathOption.isSelected() || (vpTimedOption.isSelected() && fixedVps.isSelected())) && !questBoqOption.isSelected() && readInt(vpsToAchieve.getText())<1) {
			JOptionPane.showMessageDialog(null,"VPs to Achieve must be a number greater than zero");
			return false;
		}
		
		// Populate hostPrefs
		hostPrefs.setGamePortString(gamePort.getText());
		hostPrefs.setHostName(hostName.getText());
		hostPrefs.setHostEmail(hostEmail.getText());
		hostPrefs.setSmtpHost(smtpHost.getText());
		hostPrefs.setEmailNotifications(emailNotification.isSelected());
		hostPrefs.setGameTitle(gameTitle.getText());
		hostPrefs.setGamePass(gamePass.getText());
		hostPrefs.setGameKeyVals(getSelectedGameVariant().getKeyVals());
		hostPrefs.setGameSetupName(getSelectedGameVariant().getSetup());
		hostPrefs.setNumberMonthsToPlayString(numberMonthsToPlay.getText());
		hostPrefs.setFixedVps(fixedVps.isSelected());
		hostPrefs.setVpsToAchieveString(vpsToAchieve.getText());
		hostPrefs.setEnableBattles(!disableBattles.isSelected());
		hostPrefs.setDisableSummoning(disableSummoning.isSelected());
		hostPrefs.setAutosaveEnabled(autosaveEnabled.isSelected());
		hostPrefs.setBoardAutoSetup(boardAutoSetup.isSelected());
		hostPrefs.setBoardPlayerSetup(boardPlayerSetup.isSelected());
		hostPrefs.setAlternativeTilesEnabled(alternativeTilesEnabled.isSelected());
		hostPrefs.setMixExpansionTilesEnabled(mixExpansionTilesEnabled.isSelected() && getSelectedGameVariant().getAllowBoardVariants());
		hostPrefs.setIncludeExpansionSpells(includeExpansionSpells.isSelected() && getSelectedGameVariant().getAllowBoardVariants());
		hostPrefs.setIncludeNewSpells(includeNewSpells.isSelected());
		hostPrefs.setIncludeNewSpells2(includeNewSpells2.isSelected());
		hostPrefs.setSwitchDaySpells(switchDaySpells.isSelected());
		hostPrefs.setMultiBoardEnabled(multiBoardEnabled.isSelected());
		hostPrefs.setMultiBoardCount(multiBoardCount.getValue());
		hostPrefs.setMinimumMapRating(boardAutoSetup.isSelected()?minMapRating.getValue():0);
		hostPrefs.setRequiredVPsOff(vpEndlessOption.isSelected());
		hostPrefs.setPref(Constants.QST_BOOK_OF_QUESTS,questBoqOption.isSelected());
		hostPrefs.setPref(Constants.QST_QUEST_CARDS,questQtrOption.isSelected());
		hostPrefs.setPref(Constants.QST_GUILD_QUESTS,questGuildsOption.isSelected());
		
		Object obj = startingSeason.getSelectedItem();
		if (obj instanceof GameObject) {
			GameObject go = (GameObject)startingSeason.getSelectedItem();
			hostPrefs.setStartingSeason(go.getName());
		}
		else {
			hostPrefs.setStartingSeason(obj.toString());
		}
		
		for (String optionKey : optionPane.getGameOptionKeys()) {
			hostPrefs.setPref(optionKey,optionPane.getOption(optionKey));
		}
		
		hostPrefs.setPref(Constants.OPT_WEATHER,useWeather.isSelected());
		hostPrefs.setPref(Constants.EXP_SUDDEN_DEATH,vpSuddenDeathOption.isSelected());
		hostPrefs.setPref(Constants.HOUSE2_ANY_VPS,anyVpsAllowedOption.isSelected());
		hostPrefs.setFixedVps(fixedVps.isSelected());
		
		return true;
	}
	public boolean getDidStart() {
		return didStart;
	}
	public void close() {
		setVisible(false);
		dispose();
	}
	public HostPrefWrapper getHostPrefs() {
		return hostPrefs;
	}
	public void doEmailTest() {
		String smtp = smtpHost.getText();
		String address = hostEmail.getText();
		if (smtp.trim().length()==0) {
			JOptionPane.showMessageDialog(this,"Please enter a valid SMTP host.","Error!",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (address.trim().length()==0 || address.indexOf('@')<0 || address.indexOf('.')<0) {
			JOptionPane.showMessageDialog(this,"Please enter a valid host email address.","Error!",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ArrayList<String> recipients = new ArrayList<>();
		recipients.add(address);
		String error = RealmMail.sendMail(smtp,address,recipients,gameTitle.getText(),"Test","This is a test of the e-mail notification setup within RealmSpeak.  If you receieved this e-mail by mistake, please ignore it.");
		if (error!=null) {
			JOptionPane.showMessageDialog(this,"There was a problem posting mail: "+error,"Error!",JOptionPane.ERROR_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(this,"Mail sent successfully.","Success!",JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	/**
	 * For testing only
	 */
	public static void main(String[]args) {
		ComponentTools.setSystemLookAndFeel();
		RealmLoader loader = new RealmLoader();
		HostGameSetupDialog dialog = new HostGameSetupDialog(new JFrame(),"Host New Game",loader.getData());
		dialog.loadPrefsFromLocalConfiguration();
		dialog.setVisible(true);
		System.exit(0);
	}
}