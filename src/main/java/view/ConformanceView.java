package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import algorithm.*;
import dk.brics.automaton.RegExp;
import model.*;
import parser.ImportAutFile;
import java.awt.GridLayout;
import java.awt.TextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JLayeredPane;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;

public class ConformanceView extends JFrame {
	private JComboBox cbModel;
	private JComboBox cbLabel;
	private JLabel lblImplementation;
	private JLabel lblSpecification;
	private JLabel lblOutput;
	private JLabel lblKinfModel;
	private JLabel lblInput;
	private JLabel lblLabelInp;
	private JLabel lblLabelOut;
	private JLabel lblRotulo;
	private JPanel contentPane;
	private JTextField tfImplementation;
	private JTextField tfSpecification;
	private JLabel lblD;
	private JLabel lblRegexD;
	private JLabel lblF;
	private JLabel lblRegexF;

	private String pathImplementation;
	private String pathSpecification;
	private String failPath;
	JFileChooser fc = new JFileChooser();
	private JTextField tfInput;
	private JTextField tfOutput;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private String typeAutomaticLabel = "?in, !out";
	private String typeManualLabel = "define input and output manually";
	private String LTS_CONST = "LTS";
	private String IOLTS_CONST = "IOLTS";
//	private SystemColor backgroundColor = SystemColor.controlHighlight;
//	private SystemColor labelColor = SystemColor.windowBorder;
//	private SystemColor tipColor = SystemColor.windowBorder;
//	private SystemColor borderColor = SystemColor.windowBorder;
//	private SystemColor textColor = SystemColor.controlShadow;
//	private SystemColor buttonColor = SystemColor.activeCaptionBorder;

	private SystemColor backgroundColor = SystemColor.menu;
	private SystemColor labelColor = SystemColor.windowBorder;
	private SystemColor tipColor = SystemColor.windowBorder;
	private SystemColor borderColor = SystemColor.windowBorder;
	private SystemColor textColor = SystemColor.controlShadow;
	private SystemColor buttonColor = SystemColor.activeCaptionBorder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConformanceView frame = new ConformanceView();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void configFilterFile() {
		FileFilter autFilter = new FileTypeFilter(".aut", "Aut Files");
		fc.addChoosableFileFilter(autFilter);
		fc.setAcceptAllFileFilterUsed(false);
	}

	public void cleanVeredict() {
		lbl_veredict_ioco.setText("");
		btnTestCases_ioco.setVisible(false);
		lbl_veredict_lang.setText("");
		btnTestCases_lang.setVisible(false);
	}

	public void getImplementationPath() {
		failPath = "";
		cleanVeredict();
		try {
			configFilterFile();
			fc.showOpenDialog(ConformanceView.this);
			tfImplementation.setText(fc.getSelectedFile().getName());
			pathImplementation = fc.getSelectedFile().getAbsolutePath();
			fc.setCurrentDirectory(fc.getSelectedFile().getParentFile());
		} catch (Exception e) {

		}
	}

	public void getSpecificationPath() {
		failPath = "";
		cleanVeredict();
		try {
			configFilterFile();
			fc.showOpenDialog(ConformanceView.this);
			tfSpecification.setText(fc.getSelectedFile().getName());
			pathSpecification = fc.getSelectedFile().getAbsolutePath();
		} catch (Exception e) {
		}

	}

	public boolean regexIsValid(String exp) {
		try {
			RegExp regExp = new RegExp(exp);
			regExp.toAutomaton();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	final JPanel panel = new JPanel();

	public void actionVerifyConformance(boolean ioco) {
		if (isFormValid(ioco)) {
			if (ioco) {
				iocoConformance();
			} else {
				languageBasedConformance();
			}

			if (ioco) {
				lbl_veredict_ioco.setText(Operations.veredict(conformidade));
				if (!failPath.equals("")) {
					btnTestCases_ioco.setVisible(true);
				}
			} else {
				lbl_veredict_lang.setText(Operations.veredict(conformidade));
				if (!failPath.equals("")) {
					btnTestCases_lang.setVisible(true);
				}
			}

		} else {
			errorMessage(ioco);
		}
	}

	/**
	 * Create the frame.
	 */
	public ConformanceView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/img/icon.PNG")));
		setTitle("VTool\r\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 351);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(backgroundColor);
		tabbedPane.setFont(new Font("Microsoft YaHei Light", Font.PLAIN, 13));
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_conf = new JPanel();
		panel_conf.setForeground(SystemColor.textInactiveText);
		panel_conf.setBackground(backgroundColor);
		panel_conf.setToolTipText("");
		tabbedPane.addTab("Configuration", null, panel_conf, null);
		panel_conf.setLayout(null);

		lblImplementation = new JLabel("Implementation");
		lblImplementation.setBackground(backgroundColor);
		lblImplementation.setForeground(labelColor);
		lblImplementation.setFont(new Font("Dialog", Font.BOLD, 13));
		lblImplementation.setBounds(20, 203, 157, 22);
		panel_conf.add(lblImplementation);

		lblSpecification = new JLabel("Model");
		lblSpecification.setForeground(labelColor);
		lblSpecification.setFont(new Font("Dialog", Font.BOLD, 13));
		lblSpecification.setBounds(20, 152, 99, 14);
		panel_conf.add(lblSpecification);

		tfImplementation = new JTextField();
		tfImplementation.setForeground(textColor);
		tfImplementation.setBackground(backgroundColor);
		tfImplementation.setToolTipText("aceita somente arquivos .aut");
		tfImplementation.setFont(new Font("Dialog", Font.BOLD, 13));
		tfImplementation.addMouseListener(new MouseAdapter() {
			/*
			 * @Override public void mouseClicked(MouseEvent arg0) {
			 * getImplementationPath(); }
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				getImplementationPath();
			}
		});
		tfImplementation.setColumns(10);
		tfImplementation.setBounds(20, 224, 440, 26);
		tfImplementation.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(tfImplementation);

		tfSpecification = new JTextField();
		tfSpecification.setForeground(textColor);
		tfSpecification.setBackground(backgroundColor);
		tfSpecification.setToolTipText("accepts only .aut files");
		tfSpecification.setFont(new Font("Dialog", Font.BOLD, 13));
		tfSpecification.addMouseListener(new MouseAdapter() {
			/*
			 * @Override public void mouseClicked(MouseEvent e) { getSpecificationPath(); }
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				getSpecificationPath();
			}
		});
		tfSpecification.setColumns(10);
		tfSpecification.setBounds(19, 166, 443, 26);
		tfSpecification.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(tfSpecification);

		JButton btnFolderImp = new JButton("");
		btnFolderImp.setBackground(buttonColor);
		btnFolderImp.setOpaque(true);
		btnFolderImp.addMouseListener(new MouseAdapter() {
			/*
			 * @Override public void mouseClicked(MouseEvent e) { getImplementationPath(); }
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				getImplementationPath();
			}
		});
		btnFolderImp.setIcon(new ImageIcon(this.getClass().getResource("/img/folder.png")));
		btnFolderImp.setBounds(462, 224, 39, 28);
		panel_conf.add(btnFolderImp);

		JButton btnFolderSpec = new JButton("");
		btnFolderSpec.setBackground(buttonColor);
		btnFolderSpec.setOpaque(true);
		btnFolderSpec.addMouseListener(new MouseAdapter() {
			/*
			 * @Override public void mouseClicked(MouseEvent e) { getSpecificationPath(); }
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				getSpecificationPath();
			}
		});
		btnFolderSpec.setIcon(new ImageIcon(this.getClass().getResource("/img/folder.png")));
		btnFolderSpec.setBounds(461, 164, 39, 28);
		panel_conf.add(btnFolderSpec);

		lblOutput = new JLabel("Output");
		lblOutput.setForeground(labelColor);
		lblOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOutput.setBounds(265, 68, 48, 22);
		lblOutput.setVisible(false);
		panel_conf.add(lblOutput);

		lblKinfModel = new JLabel("Kind of models");
		lblKinfModel.setForeground(labelColor);
		lblKinfModel.setFont(new Font("Dialog", Font.BOLD, 13));
		lblKinfModel.setBounds(21, 11, 104, 22);
		panel_conf.add(lblKinfModel);

		lblInput = new JLabel("Input");
		lblInput.setForeground(labelColor);
		lblInput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblInput.setBounds(21, 68, 54, 22);
		lblInput.setVisible(false);
		panel_conf.add(lblInput);

		tfInput = new JTextField();
		tfInput.setForeground(textColor);
		tfInput.setBackground(backgroundColor);
		tfInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				failPath = "";
				cleanVeredict();
			}
		});
		tfInput.setToolTipText("");
		tfInput.setFont(new Font("Dialog", Font.BOLD, 13));
		tfInput.setColumns(10);
		tfInput.setBounds(21, 88, 234, 22);
		tfInput.setVisible(false);

		tfInput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(tfInput);

		tfOutput = new JTextField();
		tfOutput.setForeground(textColor);
		tfOutput.setBackground(backgroundColor);
		tfOutput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				failPath = "";
				cleanVeredict();
			}
		});
		tfOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		tfOutput.setColumns(10);
		tfOutput.setBounds(265, 88, 236, 22);
		tfOutput.setVisible(false);
		tfOutput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(tfOutput);

		lblLabelInp = new JLabel("(label split by comma)");
		lblLabelInp.setBackground(backgroundColor);
		lblLabelInp.setForeground(tipColor);
		lblLabelInp.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLabelInp.setBounds(126, 112, 129, 14);
		lblLabelInp.setVisible(false);
		panel_conf.add(lblLabelInp);

		lblLabelOut = new JLabel("(label split by comma)");
		lblLabelOut.setBackground(backgroundColor);
		lblLabelOut.setForeground(tipColor);
		lblLabelOut.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLabelOut.setBounds(380, 112, 164, 14);
		lblLabelOut.setVisible(false);
		panel_conf.add(lblLabelOut);

		JList list = new JList();
		list.setBounds(371, 25, 1, 1);
		panel_conf.add(list);

		lblRotulo = new JLabel("Label");
		lblRotulo.setForeground(labelColor);
		lblRotulo.setFont(new Font("Dialog", Font.BOLD, 13));
		lblRotulo.setBounds(265, 11, 60, 22);
		lblRotulo.setVisible(false);
		panel_conf.add(lblRotulo);

		cbLabel = new JComboBox();
		cbLabel.setForeground(textColor);
		cbLabel.setBackground(backgroundColor);
		cbLabel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				actionCbLabel(arg0.getItem().toString());
			}
		});
		cbLabel.setModel(new DefaultComboBoxModel(new String[] { "", this.typeAutomaticLabel, this.typeManualLabel }));
		cbLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		cbLabel.setBounds(265, 31, 237, 26);
		cbLabel.setVisible(false);
		cbLabel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(cbLabel);

		cbModel = new JComboBox();
		cbModel.setForeground(textColor);
		cbModel.setBackground(backgroundColor);
		cbModel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				actionCbModel(arg0.getItem().toString());
			}
		});

		cbModel.setModel(new DefaultComboBoxModel(new String[] { "", "IOLTS", "LTS" }));
		cbModel.setFont(new Font("Dialog", Font.BOLD, 13));
		cbModel.setBounds(21, 32, 234, 26);
		cbModel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(cbModel);

		panel_ioco = new JPanel();
		tabbedPane.addTab("IOCO Conformance", null, panel_ioco, null);
		panel_ioco.setLayout(null);

		btnVerifyConf_ioco = new JButton("Verify conformance");
		btnVerifyConf_ioco.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				actionVerifyConformance(true);
			}
		});
		btnVerifyConf_ioco.setBounds(23, 25, 167, 44);
		btnVerifyConf_ioco.setFont(new Font("Dialog", Font.BOLD, 13));
		btnVerifyConf_ioco.setBackground(Color.LIGHT_GRAY);
		panel_ioco.add(btnVerifyConf_ioco);

		lblVeredict_1 = new JLabel("Veredict:");
		lblVeredict_1.setForeground(SystemColor.windowBorder);
		lblVeredict_1.setFont(new Font("Dialog", Font.BOLD, 13));
		lblVeredict_1.setBounds(177, 216, 61, 20);
		panel_ioco.add(lblVeredict_1);

		btnTestCases_ioco = new JButton("Show test cases");
		btnTestCases_ioco.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showTestCases();
			}
		});
		btnTestCases_ioco.setFont(new Font("Dialog", Font.BOLD, 13));
		btnTestCases_ioco.setBackground(Color.LIGHT_GRAY);
		btnTestCases_ioco.setBounds(366, 204, 143, 44);
		panel_ioco.add(btnTestCases_ioco);

		lbl_veredict_ioco = new JLabel("");
		lbl_veredict_ioco.setForeground(SystemColor.windowBorder);
		lbl_veredict_ioco.setFont(new Font("Dialog", Font.BOLD, 13));
		lbl_veredict_ioco.setBounds(236, 216, 133, 20);
		panel_ioco.add(lbl_veredict_ioco);

		panel_language = new JPanel();
		tabbedPane.addTab("Language Based Conformance", null, panel_language, null);
		panel_language.setLayout(null);

		lblD = new JLabel("Language D");
		lblD.setForeground(SystemColor.windowBorder);
		lblD.setFont(new Font("Dialog", Font.BOLD, 13));
		lblD.setBounds(20, 11, 96, 20);
		panel_language.add(lblD);

		tfD = new JTextField();
		tfD.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				cleanVeredict();
			}
		});
		tfD.setForeground(SystemColor.controlShadow);
		tfD.setFont(new Font("Dialog", Font.BOLD, 13));
		tfD.setColumns(10);
		tfD.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfD.setBackground(SystemColor.menu);
		tfD.setBounds(20, 28, 488, 26);
		panel_language.add(tfD);

		lblRegexD = new JLabel("(Regex: +,*,())");
		lblRegexD.setForeground(SystemColor.windowBorder);
		lblRegexD.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRegexD.setBounds(432, 46, 79, 36);
		panel_language.add(lblRegexD);

		lblF = new JLabel("Language F");
		lblF.setForeground(SystemColor.windowBorder);
		lblF.setFont(new Font("Dialog", Font.BOLD, 13));
		lblF.setBounds(20, 78, 79, 14);
		panel_language.add(lblF);

		tfF = new JTextField();
		tfF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				cleanVeredict();
			}
		});
		tfF.setForeground(SystemColor.controlShadow);
		tfF.setFont(new Font("Dialog", Font.BOLD, 13));
		tfF.setColumns(10);
		tfF.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfF.setBackground(SystemColor.menu);
		tfF.setBounds(20, 91, 488, 26);
		panel_language.add(tfF);

		lblRegexF = new JLabel("(Regex: +,*,())");
		lblRegexF.setForeground(SystemColor.windowBorder);
		lblRegexF.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRegexF.setBounds(432, 106, 93, 36);
		panel_language.add(lblRegexF);

		lblVeredict = new JLabel("Veredict:");
		lblVeredict.setForeground(SystemColor.windowBorder);
		lblVeredict.setFont(new Font("Dialog", Font.BOLD, 13));
		lblVeredict.setBounds(189, 226, 65, 20);
		panel_language.add(lblVeredict);

		btnTestCases_lang = new JButton("Show test cases");
		btnTestCases_lang.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showTestCases();
			}
		});
		btnTestCases_lang.setFont(new Font("Dialog", Font.BOLD, 13));
		btnTestCases_lang.setBackground(Color.LIGHT_GRAY);
		btnTestCases_lang.setBounds(369, 213, 140, 46);
		panel_language.add(btnTestCases_lang);

		lbl_veredict_lang = new JLabel("");
		lbl_veredict_lang.setForeground(SystemColor.windowBorder);
		lbl_veredict_lang.setFont(new Font("Dialog", Font.BOLD, 13));
		lbl_veredict_lang.setBounds(247, 226, 117, 20);
		panel_language.add(lbl_veredict_lang);

		btnVerifyConf_lang = new JButton("Verify conformance");
		btnVerifyConf_lang.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				actionVerifyConformance(false);
			}
		});
		btnVerifyConf_lang.setFont(new Font("Dialog", Font.BOLD, 13));
		btnVerifyConf_lang.setBackground(Color.LIGHT_GRAY);
		btnVerifyConf_lang.setBounds(21, 151, 174, 41);
		panel_language.add(btnVerifyConf_lang);
	}

	public void setInputOutputField(boolean visibility) {
		lblInput.setVisible(visibility);
		lblOutput.setVisible(visibility);
		tfInput.setVisible(visibility);
		lblLabelInp.setVisible(visibility);
		lblLabelOut.setVisible(visibility);
		tfOutput.setVisible(visibility);
		tfInput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfOutput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));

		// tfInput.setText("");
		// tfOutput.setText("");
		failPath = "";
		cleanVeredict();

	}

	public void actionCbLabel(String label) {
		if (label.equals(typeManualLabel)) {
			setInputOutputField(true);
		} else {
			setInputOutputField(false);
		}
	}

	public void actionCbModel(String model) {
		if (model.equals("IOLTS")) {
			cbLabel.setVisible(true);
			lblRotulo.setVisible(true);
			actionCbLabel(cbLabel.getSelectedItem().toString());
		} else {
			cbLabel.setVisible(false);
			lblRotulo.setVisible(false);
			setInputOutputField(false);
		}

		failPath = "";
		cleanVeredict();

	}

	public void setFieldRegex(boolean visibility) {
		lblD.setVisible(visibility);
		tfD.setVisible(visibility);
		lblRegexD.setVisible(visibility);
		lblF.setVisible(visibility);
		tfF.setVisible(visibility);
		lblRegexF.setVisible(visibility);
		tfD.setText("");
		tfF.setText("");
	}

	public class FileTypeFilter extends FileFilter {
		private String extension;
		private String description;

		public FileTypeFilter(String extension, String description) {
			this.extension = extension;
			this.description = description;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			}
			return file.getName().endsWith(extension);
		}

		public String getDescription() {
			return description + String.format(" (*%s)", extension);
		}
	}

	Automaton_ conformidade = null;

	public void iocoConformance() {
		conformidade = null;
		IOLTS S, I = null;
		try {
			if (cbLabel.getSelectedIndex() == 2) {// manual input/output
				S = ImportAutFile.autToIOLTS(pathSpecification, true,
						new ArrayList<String>(Arrays.asList(tfInput.getText().split(","))),
						new ArrayList<String>(Arrays.asList(tfOutput.getText().split(","))));

				I = ImportAutFile.autToIOLTS(pathImplementation, true,
						new ArrayList<String>(Arrays.asList(tfInput.getText().split(","))),
						new ArrayList<String>(Arrays.asList(tfOutput.getText().split(","))));
			} else {
				S = ImportAutFile.autToIOLTS(pathSpecification, false, new ArrayList<String>(),
						new ArrayList<String>());

				I = ImportAutFile.autToIOLTS(pathImplementation, false, new ArrayList<String>(),
						new ArrayList<String>());
			}

			conformidade = IocoConformance.verifyIOCOConformance(S, I);
			failPath = Operations.path(S, I, conformidade, true);
		} catch (Exception e_) {
			JOptionPane.showMessageDialog(panel, e_.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}

	public void languageBasedConformance() {
		boolean lts = false;
		conformidade = null;
		IOLTS S, I = null;
		LTS S_, I_ = null;
		try {

			//when the model type is not selected or IOLTS is selected but not specified how to differentiate the inputs and outputs
			if (cbModel.getSelectedIndex() == 0 || (cbLabel.getSelectedIndex() == 0 && cbModel.getSelectedItem() == IOLTS_CONST)
					|| cbModel.getSelectedItem() == LTS_CONST || (cbLabel.getSelectedItem() == typeManualLabel  && tfInput.getText().isEmpty() && tfOutput.getText().isEmpty())) {
				lts = true;
			}

			if (!lts) { // IOLTS

				if (cbLabel.getSelectedIndex() == 2) {// manual input/output
					S = ImportAutFile.autToIOLTS(pathSpecification, true,
							new ArrayList<String>(Arrays.asList(tfInput.getText().split(","))),
							new ArrayList<String>(Arrays.asList(tfOutput.getText().split(","))));

					I = ImportAutFile.autToIOLTS(pathImplementation, true,
							new ArrayList<String>(Arrays.asList(tfInput.getText().split(","))),
							new ArrayList<String>(Arrays.asList(tfOutput.getText().split(","))));
				} else {
					S = ImportAutFile.autToIOLTS(pathSpecification, false, new ArrayList<String>(),
							new ArrayList<String>());

					I = ImportAutFile.autToIOLTS(pathImplementation, false, new ArrayList<String>(),
							new ArrayList<String>());
				}

				S_ = S.toLTS();
				I_ = I.toLTS();
			} else {
				S_ = ImportAutFile.autToLTS(pathSpecification);
				I_ = ImportAutFile.autToLTS(pathImplementation);
			}

			String D = "";
			D = tfD.getText();
			if (tfD.getText().isEmpty() && tfF.getText().isEmpty()) {
				D = "(";
				for (String l : S_.getAlphabet()) {
					D += l + "+";
				}
				D = D.substring(0, D.length() - 1);
				D += ")*";

			}
			tfD.setText(D);
			String F = tfF.getText();

			if (regexIsValid(D) && regexIsValid(F)) {
				conformidade = LanguageBasedConformance.verifyLanguageConformance(S_, I_, D, F);
				failPath = Operations.path(S_, I_, conformidade, false);
			} else {
				JOptionPane.showMessageDialog(panel, "Invalid regex!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

		} catch (Exception e_) {
			JOptionPane.showMessageDialog(panel, e_.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}

	private JPanel panel_language;
	private JPanel panel_ioco;
	private JTextField tfD;
	private JTextField tfF;
	private JButton btnVerifyConf_ioco;
	private JLabel lblVeredict;
	private JLabel lblVeredict_1;
	private JButton btnTestCases_ioco;
	private JButton btnTestCases_lang;
	private JLabel lbl_veredict_lang;
	private JLabel lbl_veredict_ioco;
	private JButton btnVerifyConf_lang;

	public boolean isFormValid(boolean ioco) {

		return (!tfImplementation.getText().isEmpty() && !tfSpecification.getText().isEmpty()// implementation and
																								// specification field
				&& (cbModel.getSelectedIndex() != 0 || (!ioco || cbModel.getSelectedIndex() == 0)))
				&& (!ioco || (ioco && cbModel.getSelectedItem() == IOLTS_CONST
						&& ((cbLabel.getSelectedItem() == typeAutomaticLabel)
								|| (cbLabel.getSelectedItem() == typeManualLabel && !tfInput.getText().isEmpty()
										&& !tfOutput.getText().isEmpty()))));// model selected (IOLTS or LTS)
	}

	public void errorMessage(boolean ioco) {
		// boolean langD = tfD.getText().isEmpty();
		// boolean langF = tfF.getText().isEmpty();
		boolean implementation = tfImplementation.getText().isEmpty();
		boolean specification = tfSpecification.getText().isEmpty();
		// boolean typeOfConf = (!rbConfBasedLang.isSelected() && !rbIoco.isSelected());

		boolean model = cbModel.getSelectedIndex() == 0;

		String msg = "";
		// msg += typeOfConf ? "Select the type of conformance [IOCO] or [Baseada em
		// Linguagem] \n" : "";
		msg += model ? "Select the kind of model \n" : "";
		msg += implementation ? "The field Implementation is required \n" : "";
		msg += specification ? "The field Model is required \n" : "";
		// msg += langD && langF ? "The Language D field or F language is required \n" :
		// "";

		if (ioco) {
			boolean ioltsLabel = cbLabel.getSelectedIndex() == 0 && cbModel.getSelectedItem() == IOLTS_CONST;
			msg += ioltsLabel
					? "You choosed IOCO conformance, therefore it is necessary how the IOLTS labels will be distinguished \n"
					: "";
			boolean defInpuOut = (cbModel.getSelectedItem() == IOLTS_CONST
					&& cbLabel.getSelectedItem() == typeManualLabel && tfInput.getText().isEmpty()
					&& tfOutput.getText().isEmpty());
			msg += defInpuOut ? "You choosed IOCO conformance, therefore the fields Input and Output is required \n"
					: "";
			boolean lts = cbModel.getSelectedItem() == LTS_CONST;
			msg += lts ? "You choosed IOCO conformance, the informed model must be IOLTS" : "";
		}

		JOptionPane.showMessageDialog(panel, msg, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	public void showTestCases() {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(500, 500);
		JPanel panel = new JPanel();
		TextArea ta = new TextArea(25, 60);

		ta.setText(failPath);
		JScrollPane scrolltxt = new JScrollPane(ta);
		scrolltxt.setBounds(3, 3, 400, 400);

		panel.add(scrolltxt);
		frame.getContentPane().add(panel);
	}
}
