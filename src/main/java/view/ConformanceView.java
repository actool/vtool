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
import javax.swing.UIManager;

public class ConformanceView extends JFrame {
	private JComboBox cbModel;
	private JComboBox cbLabel;
	private JLabel lblImplementation;
	private JLabel lblSpecification;
	private JLabel lblD;
	private JLabel lblF;
	private JLabel lblResult;
	private JLabel lblRegexD;
	private JLabel lblRegexF;
	private JLabel lblOutput;
	private JLabel lblKinfModel;
	private JLabel lblInput;
	private JLabel lblLabelInp;
	private JLabel lblLabelOut;
	private JLabel lblConformanceType;
	private JLabel lblRotulo;
	private JPanel contentPane;
	private JTextField tfImplementation;
	private JTextField tfSpecification;
	private JTextField tfD;
	private JTextField tfF;
	private JTextField tfVeredict;
	private JButton btnTestCases;
	private JRadioButton rbConfBasedLang;
	private JRadioButton rbIoco;

	private String pathImplementation;
	private String pathSpecification;
	private String failPath;
	JFileChooser fc = new JFileChooser();
	private JTextField tfInput;
	private JTextField tfOutput;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private String typeAutomaticLabel = "?in, !out";
	private String typeManualLabel = "define input and output manually";
	// private SystemColor backgroundColor = SystemColor.windowBorder;
	// private SystemColor fontColor = SystemColor.controlHighlight;
	// private SystemColor tipColor = SystemColor.scrollbar;
	// private SystemColor borderColor = SystemColor.control;

	private SystemColor backgroundColor = SystemColor.controlHighlight;
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

	public void getImplementationPath() {
		failPath = "";
		tfVeredict.setText("");
		btnTestCases.setVisible(false);
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
		tfVeredict.setText("");
		btnTestCases.setVisible(false);
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

	/**
	 * Create the frame.
	 */
	public ConformanceView() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource("/img/icon.PNG")));
		setTitle("VTool\r\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 556, 625);
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

		JPanel panel_2 = new JPanel();
		panel_2.setForeground(SystemColor.textInactiveText);
		panel_2.setBackground(backgroundColor);
		panel_2.setToolTipText("");
		tabbedPane.addTab("Conformance Verification", null, panel_2, null);
		panel_2.setLayout(null);

		lblImplementation = new JLabel("Implementation");
		lblImplementation.setBackground(backgroundColor);
		lblImplementation.setForeground(labelColor);
		lblImplementation.setFont(new Font("Dialog", Font.BOLD, 13));
		lblImplementation.setBounds(20, 190, 157, 22);
		panel_2.add(lblImplementation);

		lblSpecification = new JLabel("Model");
		lblSpecification.setForeground(labelColor);
		lblSpecification.setFont(new Font("Dialog", Font.BOLD, 13));
		lblSpecification.setBounds(21, 247, 99, 14);
		panel_2.add(lblSpecification);

		tfImplementation = new JTextField();
		tfImplementation.setForeground(textColor);
		tfImplementation.setBackground(backgroundColor);
		tfImplementation.setToolTipText("aceita somente arquivos .aut");
		tfImplementation.setFont(new Font("Dialog", Font.BOLD, 13));
		tfImplementation.addMouseListener(new MouseAdapter() {
			/*@Override
			public void mouseClicked(MouseEvent arg0) {
				getImplementationPath();
			}*/
			@Override
			public void mousePressed(MouseEvent e) {
				getImplementationPath();
			}
		});
		tfImplementation.setColumns(10);
		tfImplementation.setBounds(20, 211, 440, 26);
		tfImplementation.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_2.add(tfImplementation);

		tfSpecification = new JTextField();
		tfSpecification.setForeground(textColor);
		tfSpecification.setBackground(backgroundColor);
		tfSpecification.setToolTipText("accepts only .aut files");
		tfSpecification.setFont(new Font("Dialog", Font.BOLD, 13));
		tfSpecification.addMouseListener(new MouseAdapter() {
			/*@Override
			public void mouseClicked(MouseEvent e) {
				getSpecificationPath();
			}*/
			@Override
			public void mousePressed(MouseEvent e) {
				getSpecificationPath();
			}
		});
		tfSpecification.setColumns(10);
		tfSpecification.setBounds(20, 261, 443, 26);
		tfSpecification.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_2.add(tfSpecification);

		lblD = new JLabel("Language D");
		lblD.setForeground(labelColor);
		lblD.setFont(new Font("Dialog", Font.BOLD, 13));
		lblD.setBounds(21, 298, 96, 20);
		panel_2.add(lblD);

		lblF = new JLabel("Language F");
		lblF.setForeground(labelColor);
		lblF.setFont(new Font("Dialog", Font.BOLD, 13));
		lblF.setBounds(21, 364, 79, 14);
		panel_2.add(lblF);

		tfD = new JTextField();
		tfD.setForeground(textColor);
		tfD.setBackground(backgroundColor);
		tfD.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				failPath = "";
				tfVeredict.setText("");
				btnTestCases.setVisible(false);
			}
		});
		tfD.setFont(new Font("Dialog", Font.BOLD, 13));
		tfD.setBounds(21, 315, 487, 26);
		tfD.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_2.add(tfD);
		tfD.setColumns(10);

		tfF = new JTextField();
		tfF.setForeground(textColor);
		tfF.setBackground(backgroundColor);
		tfF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				failPath = "";
				tfVeredict.setText("");
				btnTestCases.setVisible(false);
			}
		});
		tfF.setFont(new Font("Dialog", Font.BOLD, 13));
		tfF.setBounds(21, 378, 487, 26);
		tfF.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_2.add(tfF);
		tfF.setColumns(10);

		tfVeredict = new JTextField();
		tfVeredict.setFont(new Font("Dialog", Font.BOLD, 13));
		tfVeredict.setEnabled(false);
		tfVeredict.setBounds(20, 494, 285, 36);
		panel_2.add(tfVeredict);
		tfVeredict.setColumns(10);

		JButton btnVerifyConformance = new JButton("Verify conformance");
		btnVerifyConformance.setBackground(new Color(192, 192, 192));
		btnVerifyConformance.setFont(new Font("Dialog", Font.BOLD, 13));

		btnVerifyConformance.setBounds(315, 430, 193, 45);
		panel_2.add(btnVerifyConformance);

		lblResult = new JLabel("Veredict");
		lblResult.setForeground(labelColor);
		lblResult.setFont(new Font("Dialog", Font.BOLD, 13));
		lblResult.setBounds(20, 468, 102, 26);
		panel_2.add(lblResult);

		lblRegexD = new JLabel("(Regex: +,*,())");
		lblRegexD.setForeground(tipColor);
		lblRegexD.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRegexD.setBounds(432, 333, 79, 36);
		panel_2.add(lblRegexD);

		lblRegexF = new JLabel("(Regex: +,*,())");
		lblRegexF.setForeground(tipColor);
		lblRegexF.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRegexF.setBounds(432, 393, 93, 36);
		panel_2.add(lblRegexF);

		btnTestCases = new JButton("Show test cases");
		btnTestCases.setBackground(buttonColor);
		btnTestCases.setOpaque(true);
		btnTestCases.setVisible(false);
		btnTestCases.addMouseListener(new MouseAdapter() {
			/*@Override
			public void mouseClicked(MouseEvent e) {
				
			}*/
			@Override
			public void mousePressed(MouseEvent e) {
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
		});
		btnTestCases.setFont(new Font("Dialog", Font.BOLD, 13));
		btnTestCases.setBounds(315, 494, 193, 36);
		panel_2.add(btnTestCases);

		JButton btnFolderImp = new JButton("");
		btnFolderImp.setBackground(buttonColor);
		btnFolderImp.setOpaque(true);
		btnFolderImp.addMouseListener(new MouseAdapter() {
			/*@Override
			public void mouseClicked(MouseEvent e) {
				getImplementationPath();
			}*/
			@Override
			public void mousePressed(MouseEvent e) {
				getImplementationPath();
			}
		});
		btnFolderImp.setIcon(new ImageIcon(this.getClass().getResource("/img/folder.png")));
		btnFolderImp.setBounds(462, 211, 39, 28);
		panel_2.add(btnFolderImp);

		JButton btnFolderSpec = new JButton("");
		btnFolderSpec.setBackground(buttonColor);
		btnFolderSpec.setOpaque(true);
		btnFolderSpec.addMouseListener(new MouseAdapter() {
			/*@Override
			public void mouseClicked(MouseEvent e) {
				getSpecificationPath();
			}*/
			@Override
			public void mousePressed(MouseEvent e) {
				getSpecificationPath();
			}
		});
		btnFolderSpec.setIcon(new ImageIcon(
				this.getClass().getResource("/img/folder.png")));
		btnFolderSpec.setBounds(462, 259, 39, 28);
		panel_2.add(btnFolderSpec);

		lblOutput = new JLabel("Output");
		lblOutput.setForeground(labelColor);
		lblOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOutput.setBounds(265, 121, 48, 22);
		lblOutput.setVisible(false);
		panel_2.add(lblOutput);

		lblKinfModel = new JLabel("Kind of models");
		lblKinfModel.setForeground(labelColor);
		lblKinfModel.setFont(new Font("Dialog", Font.BOLD, 13));
		lblKinfModel.setBounds(21, 64, 104, 22);
		panel_2.add(lblKinfModel);

		lblInput = new JLabel("Input");
		lblInput.setForeground(labelColor);
		lblInput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblInput.setBounds(21, 121, 54, 22);
		lblInput.setVisible(false);
		panel_2.add(lblInput);

		tfInput = new JTextField();
		tfInput.setForeground(textColor);
		tfInput.setBackground(backgroundColor);
		tfInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				failPath = "";
				tfVeredict.setText("");
				btnTestCases.setVisible(false);
			}
		});
		tfInput.setToolTipText("");
		tfInput.setFont(new Font("Dialog", Font.BOLD, 13));
		tfInput.setColumns(10);
		tfInput.setBounds(21, 141, 234, 22);
		tfInput.setVisible(false);

		tfInput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_2.add(tfInput);

		tfOutput = new JTextField();
		tfOutput.setForeground(textColor);
		tfOutput.setBackground(backgroundColor);
		tfOutput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				failPath = "";
				tfVeredict.setText("");
				btnTestCases.setVisible(false);
			}
		});
		tfOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		tfOutput.setColumns(10);
		tfOutput.setBounds(265, 141, 236, 22);
		tfOutput.setVisible(false);
		tfOutput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_2.add(tfOutput);

		lblLabelInp = new JLabel("(label split by comma)");
		lblLabelInp.setBackground(backgroundColor);
		lblLabelInp.setForeground(tipColor);
		lblLabelInp.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLabelInp.setBounds(126, 165, 129, 14);
		lblLabelInp.setVisible(false);
		panel_2.add(lblLabelInp);

		lblLabelOut = new JLabel("(label split by comma)");
		lblLabelOut.setBackground(backgroundColor);
		lblLabelOut.setForeground(tipColor);
		lblLabelOut.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLabelOut.setBounds(380, 165, 164, 14);
		lblLabelOut.setVisible(false);
		panel_2.add(lblLabelOut);

		JList list = new JList();
		list.setBounds(371, 25, 1, 1);
		panel_2.add(list);

		lblConformanceType = new JLabel("Conformance");
		lblConformanceType.setForeground(labelColor);
		lblConformanceType.setToolTipText("");
		lblConformanceType.setFont(new Font("Microsoft JhengHei Light", Font.BOLD, 13));
		lblConformanceType.setBounds(21, 18, 129, 22);
		panel_2.add(lblConformanceType);

		lblRotulo = new JLabel("Label");
		lblRotulo.setForeground(labelColor);
		lblRotulo.setFont(new Font("Dialog", Font.BOLD, 13));
		lblRotulo.setBounds(265, 64, 60, 22);
		lblRotulo.setVisible(false);
		panel_2.add(lblRotulo);

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
		cbLabel.setBounds(265, 84, 237, 26);
		cbLabel.setVisible(false);
		cbLabel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_2.add(cbLabel);

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
		cbModel.setBounds(21, 85, 234, 26);
		cbModel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_2.add(cbModel);

		rbIoco = new JRadioButton("IOCO");
		rbIoco.setForeground(labelColor);
		rbIoco.setBackground(backgroundColor);
		rbIoco.addMouseListener(new MouseAdapter() {
			/*@Override
			public void mouseClicked(MouseEvent arg0) {
				actionRbIoco();
			}*/
			@Override
			public void mousePressed(MouseEvent e) {
				actionRbIoco();
			}
		});

		buttonGroup.add(rbIoco);
		rbIoco.setFont(new Font("Dialog", Font.BOLD, 13));
		rbIoco.setBounds(142, 18, 85, 23);
		panel_2.add(rbIoco);

		rbConfBasedLang = new JRadioButton("Based on language");
		rbConfBasedLang.setForeground(labelColor);
		rbConfBasedLang.setBackground(backgroundColor);
		rbConfBasedLang.addMouseListener(new MouseAdapter() {
			/*@Override
			public void mouseClicked(MouseEvent arg0) {
				actionRbLanguage();
			}*/
			@Override
			public void mousePressed(MouseEvent e) {
				actionRbLanguage();
			}
		});
		buttonGroup.add(rbConfBasedLang);
		rbConfBasedLang.setFont(new Font("Dialog", Font.BOLD, 13));
		rbConfBasedLang.setBounds(229, 18, 237, 23);
		panel_2.add(rbConfBasedLang);

		btnVerifyConformance.addMouseListener(new MouseAdapter() {
			/*@Override
			public void mouseClicked(MouseEvent e) {
				actionVerifyConformance();
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				actionVerifyConformance();
			}*/
			@Override
			public void mousePressed(MouseEvent e) {
				actionVerifyConformance();
			}
		});
	}

	public void setInputOutputField(boolean visibility) {
		lblInput.setVisible(visibility);
		lblOutput.setVisible(visibility);
		tfInput.setVisible(visibility);
		lblLabelInp.setVisible(visibility);
		lblLabelOut.setVisible(visibility);
		tfOutput.setVisible(visibility);

		tfInput.setText("");
		tfOutput.setText("");
		failPath = "";
		tfVeredict.setText("");
		btnTestCases.setVisible(false);

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
		} else {
			cbLabel.setVisible(false);
			lblRotulo.setVisible(false);
			tfInput.setVisible(false);
			tfOutput.setVisible(false);
		}

		failPath = "";
		tfVeredict.setText("");
		btnTestCases.setVisible(false);

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

	public void actionRbLanguage() {
		cbModel.setEnabled(true);
		lblRotulo.setVisible(false);
		cbLabel.setVisible(false);
		cbModel.setSelectedIndex(0);
		cbLabel.setSelectedIndex(0);

		setFieldRegex(true);
		setInputOutputField(false);
	}

	public void actionRbIoco() {
		cbModel.setEnabled(false);
		cbModel.setSelectedIndex(1);
		lblRotulo.setVisible(true);
		cbLabel.setVisible(true);
		cbLabel.setSelectedIndex(0);

		failPath = "";
		tfVeredict.setText("");
		btnTestCases.setVisible(false);

		setFieldRegex(false);
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

	public boolean isFormValid() {

		return !tfImplementation.getText().isEmpty() && !tfSpecification.getText().isEmpty()
				&& (rbConfBasedLang.isSelected() || rbIoco.isSelected()) && cbModel.getSelectedIndex() != 0
				&& ((cbLabel.getSelectedIndex() != 0 && cbModel.getSelectedIndex() == 1)
						|| (cbModel.getSelectedIndex() == 1 && cbLabel.getSelectedIndex() == 1
								&& !tfInput.getText().isEmpty() && !tfOutput.getText().isEmpty())
						|| (cbModel.getSelectedIndex() == 2));
	}

	
	boolean withoutRegex = false;
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
			failPath = Operations.path(S, I, conformidade);
		} catch (Exception e_) {
			JOptionPane.showMessageDialog(panel, e_.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
	

	public void languageBasedConformance() {
		withoutRegex = false;
		conformidade = null;
		IOLTS S, I = null;
		LTS S_, I_ = null;
		try {
			if (cbModel.getSelectedIndex() == 1) { // IOLTS

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

				withoutRegex = true;
			}

			String F = tfF.getText();

			if (regexIsValid(D) && regexIsValid(F)) {
				conformidade = LanguageBasedConformance.verifyLanguageConformance(S_, I_, D, F);
				failPath = Operations.path(S_, I_, conformidade);
			} else {
				JOptionPane.showMessageDialog(panel, "Invalid regex!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

		} catch (Exception e_) {
			JOptionPane.showMessageDialog(panel, e_.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
	
	final JPanel panel = new JPanel();
	public void actionVerifyConformance() {
		if (isFormValid()) {
			if (rbIoco.isSelected()) {

			iocoConformance();

			} else {
				languageBasedConformance();
			}

			tfVeredict.setText(Operations.veredict(conformidade));

			if (!failPath.equals("")) {
				btnTestCases.setVisible(true);
			}

			if (withoutRegex) {
				JOptionPane.showMessageDialog(panel,
						"Test suite is empty, because the languages D and F were not informed!", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else {
			errorMessage();
		}
	}

	public void errorMessage() {
		// boolean langD = tfD.getText().isEmpty();
		// boolean langF = tfF.getText().isEmpty();
		boolean implementation = tfImplementation.getText().isEmpty();
		boolean specification = tfSpecification.getText().isEmpty();
		boolean typeOfConf = (!rbConfBasedLang.isSelected() && !rbIoco.isSelected());
		boolean ioltsLabel = cbLabel.getSelectedIndex() == 0 && cbModel.getSelectedIndex() == 1;
		boolean defInpuOut = (cbLabel.getSelectedIndex() == 1 && tfInput.getText().isEmpty()
				&& tfOutput.getText().isEmpty());
		boolean model = cbModel.getSelectedIndex() == 0;

		String msg = "";

		msg += typeOfConf ? "Select the type of conformance [IOCO] or [Baseada em Linguagem] \n" : "";
		msg += model ? "Select the kind of model \n" : "";
		msg += ioltsLabel ? "Select how the IOLTS labels will be distinguished \n" : "";
		msg += defInpuOut ? "The fields Input and Output is required \n" : "";
		msg += implementation ? "The field Implementation is required \n" : "";
		msg += specification ? "The field Model is required \n" : "";
		// msg += langD && langF ? "The Language D field or F language is required \n" :
		// "";

		JOptionPane.showMessageDialog(panel, msg, "Warning", JOptionPane.WARNING_MESSAGE);
	}
}
