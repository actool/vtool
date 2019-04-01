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

public class ConformanceView extends JFrame {

	private JPanel contentPane;
	private JTextField tfImplementation;
	private JTextField tfSpecification;
	private JTextField tfD;
	private JTextField tfF;
	private JTextField tfVeredict;
	private JButton btnTestCases;

	private String pathImplementation;
	private String pathSpecification;
	private String failPath;
	JFileChooser fc = new JFileChooser();
	private JTextField tfInput;
	private JTextField tfOutput;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private String typeAutomaticLabel = "?in, !out";
	private String typeManualLabel = "define input and output manually";

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
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\camil\\Google Drive\\UEL\\svn\\mbt\\camila_mestrado\\img\\icon.PNG"));
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
		tabbedPane.setBackground(SystemColor.windowBorder);
		tabbedPane.setFont(new Font("Microsoft YaHei Light", Font.PLAIN, 13));
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel_2.setForeground(SystemColor.textInactiveText);
		panel_2.setBackground(SystemColor.windowBorder);
		panel_2.setToolTipText("");
		tabbedPane.addTab("Conformance Verification", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblImplementation = new JLabel("Implementation");
		lblImplementation.setBackground(SystemColor.windowBorder);
		lblImplementation.setForeground(SystemColor.controlHighlight);
		lblImplementation.setFont(new Font("Dialog", Font.BOLD, 13));
		lblImplementation.setBounds(20, 190, 157, 22);
		panel_2.add(lblImplementation);

		JLabel lblSpecification = new JLabel("Model");
		lblSpecification.setForeground(SystemColor.controlHighlight);
		lblSpecification.setFont(new Font("Dialog", Font.BOLD, 13));
		lblSpecification.setBounds(21, 247, 99, 14);
		panel_2.add(lblSpecification);

		tfImplementation = new JTextField();
		tfImplementation.setForeground(SystemColor.window);
		tfImplementation.setBackground(SystemColor.windowBorder);
		tfImplementation.setToolTipText("aceita somente arquivos .aut");
		tfImplementation.setFont(new Font("Dialog", Font.BOLD, 13));
		tfImplementation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				getImplementationPath();
			}
		});
		tfImplementation.setColumns(10);
		tfImplementation.setBounds(20, 211, 440, 26);			
		tfImplementation.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfImplementation);

		tfSpecification = new JTextField();
		tfSpecification.setForeground(SystemColor.control);
		tfSpecification.setBackground(SystemColor.windowBorder);
		tfSpecification.setToolTipText("accepts only .aut files");
		tfSpecification.setFont(new Font("Dialog", Font.BOLD, 13));
		tfSpecification.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getSpecificationPath();
			}
		});
		tfSpecification.setColumns(10);
		tfSpecification.setBounds(20, 261, 443, 26);
		tfSpecification.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfSpecification);

		JLabel lblD = new JLabel("Language D");
		lblD.setForeground(SystemColor.controlHighlight);
		lblD.setFont(new Font("Dialog", Font.BOLD, 13));
		lblD.setBounds(21, 298, 96, 20);
		panel_2.add(lblD);

		JLabel lblF = new JLabel("Language F");
		lblF.setForeground(SystemColor.controlHighlight);
		lblF.setFont(new Font("Dialog", Font.BOLD, 13));
		lblF.setBounds(21, 364, 79, 14);
		panel_2.add(lblF);

		tfD = new JTextField();
		tfD.setForeground(SystemColor.control);
		tfD.setBackground(SystemColor.windowBorder);
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
		tfD.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfD);
		tfD.setColumns(10);

		tfF = new JTextField();
		tfF.setForeground(SystemColor.control);
		tfF.setBackground(SystemColor.windowBorder);
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
		tfF.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
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

		JLabel lblResult = new JLabel("Veredict");
		lblResult.setForeground(SystemColor.controlHighlight);
		lblResult.setFont(new Font("Dialog", Font.BOLD, 13));
		lblResult.setBounds(20, 468, 102, 26);
		panel_2.add(lblResult);

		JLabel lblRegexD = new JLabel("(Regex: +,*,())");
		lblRegexD.setForeground(SystemColor.scrollbar);
		lblRegexD.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRegexD.setBounds(432, 333, 79, 36);
		panel_2.add(lblRegexD);

		JLabel lblRegexF = new JLabel("(Regex: +,*,())");
		lblRegexF.setForeground(SystemColor.scrollbar);
		lblRegexF.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRegexF.setBounds(432, 393, 93, 36);
		panel_2.add(lblRegexF);

		btnTestCases = new JButton("Show test cases");
		btnTestCases.setBackground(SystemColor.activeCaptionBorder);
		btnTestCases.setVisible(false);
		btnTestCases.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
		btnFolderImp.setBackground(SystemColor.activeCaptionBorder);
		btnFolderImp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getImplementationPath();
			}
		});
		btnFolderImp.setIcon(
				new ImageIcon("C:\\Users\\camil\\Google Drive\\UEL\\svn\\mbt\\camila_mestrado\\img\\folder.png"));
		btnFolderImp.setBounds(462, 211, 39, 28);
		panel_2.add(btnFolderImp);

		JButton btnFolderSpec = new JButton("");
		btnFolderSpec.setBackground(SystemColor.activeCaptionBorder);
		btnFolderSpec.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getSpecificationPath();
			}
		});
		btnFolderSpec.setIcon(
				new ImageIcon("C:\\Users\\camil\\Google Drive\\UEL\\svn\\mbt\\camila_mestrado\\img\\folder.png"));
		btnFolderSpec.setBounds(462, 259, 39, 28);
		panel_2.add(btnFolderSpec);

		JLabel lblOutput = new JLabel("Output");
		lblOutput.setForeground(SystemColor.controlHighlight);
		lblOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOutput.setBounds(265, 121, 48, 22);
		lblOutput.setVisible(false);
		panel_2.add(lblOutput);

		JLabel lblKinfModel = new JLabel("Kind of models");
		lblKinfModel.setForeground(SystemColor.controlHighlight);
		lblKinfModel.setFont(new Font("Dialog", Font.BOLD, 13));
		lblKinfModel.setBounds(21, 64, 104, 22);
		panel_2.add(lblKinfModel);

		JLabel lblInput = new JLabel("Input");
		lblInput.setForeground(SystemColor.controlHighlight);
		lblInput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblInput.setBounds(21, 121, 54, 22);
		lblInput.setVisible(false);
		panel_2.add(lblInput);

		tfInput = new JTextField();
		tfInput.setForeground(SystemColor.control);
		tfInput.setBackground(SystemColor.windowBorder);
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

		tfInput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfInput);

		tfOutput = new JTextField();
		tfOutput.setForeground(SystemColor.control);
		tfOutput.setBackground(SystemColor.windowBorder);
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
		tfOutput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfOutput);

		JLabel lblLabelInp = new JLabel("(label split by comma)");
		lblLabelInp.setBackground(SystemColor.windowBorder);
		lblLabelInp.setForeground(SystemColor.scrollbar);
		lblLabelInp.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLabelInp.setBounds(126, 165, 129, 14);
		lblLabelInp.setVisible(false);
		panel_2.add(lblLabelInp);

		JLabel lblLabelOut = new JLabel("(label split by comma)");
		lblLabelOut.setBackground(SystemColor.windowBorder);
		lblLabelOut.setForeground(SystemColor.scrollbar);
		lblLabelOut.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLabelOut.setBounds(380, 165, 164, 14);
		lblLabelOut.setVisible(false);
		panel_2.add(lblLabelOut);

		JList list = new JList();
		list.setBounds(371, 25, 1, 1);
		panel_2.add(list);

		JLabel lblConformanceType = new JLabel("Conformance");
		lblConformanceType.setForeground(SystemColor.controlHighlight);
		lblConformanceType.setToolTipText("");
		lblConformanceType.setFont(new Font("Microsoft JhengHei Light", Font.BOLD, 13));
		lblConformanceType.setBounds(21, 18, 129, 22);
		panel_2.add(lblConformanceType);

		JLabel lblRotulo = new JLabel("Label");
		lblRotulo.setForeground(SystemColor.controlHighlight);
		lblRotulo.setFont(new Font("Dialog", Font.BOLD, 13));
		lblRotulo.setBounds(265, 64, 60, 22);
		lblRotulo.setVisible(false);
		panel_2.add(lblRotulo);

		JComboBox cbLabel = new JComboBox();
		cbLabel.setForeground(SystemColor.control);
		cbLabel.setBackground(SystemColor.windowBorder);
		cbLabel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getItem().equals(typeManualLabel)) {
					lblInput.setVisible(true);
					lblOutput.setVisible(true);
					tfInput.setVisible(true);
					lblLabelInp.setVisible(true);
					lblLabelOut.setVisible(true);
					tfOutput.setVisible(true);
				} else {
					lblInput.setVisible(false);
					lblOutput.setVisible(false);
					tfInput.setVisible(false);
					tfOutput.setVisible(false);
					lblLabelInp.setVisible(false);
					lblLabelOut.setVisible(false);
				}

				tfInput.setText("");
				tfOutput.setText("");
				
				failPath = "";
				tfVeredict.setText("");
				btnTestCases.setVisible(false);
			}
		});
		cbLabel.setModel(
				new DefaultComboBoxModel(new String[] { "", this.typeAutomaticLabel, this.typeManualLabel }));
		cbLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		cbLabel.setBounds(265, 84, 237, 26);
		cbLabel.setVisible(false);
		cbLabel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(cbLabel);

		JComboBox cbModel = new JComboBox();
		cbModel.setForeground(SystemColor.control);
		cbModel.setBackground(SystemColor.windowBorder);
		cbModel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getItem().equals("IOLTS")) {
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
		});

		cbModel.setModel(new DefaultComboBoxModel(new String[] { "", "IOLTS", "LTS" }));
		cbModel.setFont(new Font("Dialog", Font.BOLD, 13));
		cbModel.setBounds(21, 85, 234, 26);
		cbModel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(cbModel);

		JRadioButton rbIoco = new JRadioButton("IOCO");
		rbIoco.setForeground(SystemColor.controlHighlight);
		rbIoco.setBackground(SystemColor.windowBorder);
		rbIoco.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				cbModel.setEnabled(false);
				cbModel.setSelectedIndex(1);
				lblRotulo.setVisible(true);
				cbLabel.setVisible(true);
				cbLabel.setSelectedIndex(0);
				tfD.enable(false);
				tfD.setText("");
				tfF.enable(false);
				tfF.setText("");

				failPath = "";
				tfVeredict.setText("");
				btnTestCases.setVisible(false);
			}
		});

		buttonGroup.add(rbIoco);
		rbIoco.setFont(new Font("Dialog", Font.BOLD, 13));
		rbIoco.setBounds(142, 18, 85, 23);
		panel_2.add(rbIoco);

		JRadioButton rbConfBasedLang = new JRadioButton("Based on language");
		rbConfBasedLang.setForeground(SystemColor.controlHighlight);
		rbConfBasedLang.setBackground(SystemColor.windowBorder);
		rbConfBasedLang.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				cbModel.setEnabled(true);
				lblRotulo.setVisible(false);
				cbLabel.setVisible(false);
				cbModel.setSelectedIndex(0);
				tfD.enable(true);
				tfD.setText("");
				tfF.enable(true);
				tfF.setText("");
				tfInput.setVisible(false);
				tfOutput.setVisible(false);
				lblInput.setVisible(false);
				lblOutput.setVisible(false);
				lblLabelInp.setVisible(false);
				lblLabelOut.setVisible(false);

				failPath = "";
				tfVeredict.setText("");
				btnTestCases.setVisible(false);
				
				cbLabel.setSelectedIndex(0);
				tfInput.setText("");
				tfOutput.setText("");
			}
		});
		buttonGroup.add(rbConfBasedLang);
		rbConfBasedLang.setFont(new Font("Dialog", Font.BOLD, 13));
		rbConfBasedLang.setBounds(229, 18, 237, 23);
		panel_2.add(rbConfBasedLang);

		btnVerifyConformance.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (!tfImplementation.getText().isEmpty() && !tfSpecification.getText().isEmpty()
						&& (rbConfBasedLang.isSelected() || rbIoco.isSelected()) && cbModel.getSelectedIndex() != 0
						&& ((cbLabel.getSelectedIndex() != 0 && cbModel.getSelectedIndex() == 1)
								|| (cbModel.getSelectedIndex() == 1 && cbLabel.getSelectedIndex() == 1
										&& !tfInput.getText().isEmpty() && !tfOutput.getText().isEmpty())
								|| (cbModel.getSelectedIndex() == 2))) {
					Automaton_ conformidade = null;
					IOLTS S, I = null;
					LTS S_, I_ = null;
					boolean semLinguagem = false;

					if (rbIoco.isSelected()) {// IOCO

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
							JOptionPane.showMessageDialog(panel, e_.getMessage(), "Warning",
									JOptionPane.WARNING_MESSAGE);
							return;
						}

					} else {// conformance language based
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
								
								semLinguagem = true;
							}
							
							

							String F = tfF.getText();
							
							if(regexIsValid(D) && regexIsValid(F)) {
								conformidade = LanguageBasedConformance.verifyLanguageConformance(S_, I_, D, F);
								failPath = Operations.path(S_, I_, conformidade);
							}else {
								JOptionPane.showMessageDialog(panel, "Invalid regex!", "Warning",
										JOptionPane.WARNING_MESSAGE);
								return;
							}
							
						} catch (Exception e_) {
							JOptionPane.showMessageDialog(panel, e_.getMessage(), "Warning",
									JOptionPane.WARNING_MESSAGE);
							return;
						}

					}

					tfVeredict.setText(Operations.veredict(conformidade));
					
					if(!failPath.equals("")) {
						btnTestCases.setVisible(true);
					}

					if (semLinguagem) {
						JOptionPane.showMessageDialog(panel,
								"Test suite is empty, because the languages D and F were not informed!", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					// boolean langD = tfD.getText().isEmpty();
					// boolean langF = tfF.getText().isEmpty();
					boolean implementacao = tfImplementation.getText().isEmpty();
					boolean especificacao = tfSpecification.getText().isEmpty();
					boolean tipoConf = (!rbConfBasedLang.isSelected() && !rbIoco.isSelected());
					boolean rotuloIOLTS = cbLabel.getSelectedIndex() == 0 && cbModel.getSelectedIndex() == 1;
					boolean defEntradaSaida = (cbLabel.getSelectedIndex() == 1 && tfInput.getText().isEmpty()
							&& tfOutput.getText().isEmpty());
					boolean modelo = cbModel.getSelectedIndex() == 0;

					String msg = "";

					msg += implementacao ? "The field Implementation is required \n" : "";
					msg += especificacao ? "The field Model is required \n" : "";
					// msg += langD && langF ? "The Language D field or F language is required \n" : "";
					msg += tipoConf ? "Select the type of conformance [IOCO] or [Baseada em Linguagem] \n" : "";
					msg += rotuloIOLTS ? "Select how the IOLTS labels will be distinguished \n" : "";
					msg += defEntradaSaida ? "The fields Input and Output is required \n" : "";
					msg += modelo ? "Select the Model \n" : "";

					final JPanel panel = new JPanel();
					JOptionPane.showMessageDialog(panel, msg, "Warning", JOptionPane.WARNING_MESSAGE);
				}

			}
		});
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
}
