package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import algorithm.*;
import dk.brics.automaton.RegExp;
import javafx.stage.FileChooser;
import model.*;
import parser.ImportAutFile;
import util.AutGenerator;
import util.Constants;
import util.ModelImageGenerator;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import javax.swing.JLabel;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import java.text.SimpleDateFormat;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.border.MatteBorder;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JTextArea;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JRadioButton;

public class ConformanceView extends JFrame {
	private JComboBox cbModel;
	private JComboBox cbLabel;
	private JLabel lblImplementation;
	private JLabel lblSpecification;
	private JLabel lblOutput;
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
	private JLabel lblmodelIoco;
	private JLabel lblImplementationIoco;
	private JButton btnViewModelIoco;
	private JButton btnViewImplementationIoco;
	private JButton btnViewModelLang;
	private JButton btnViewImplementationLang;
	private JLabel lblLabelIoco;
	private JLabel lblLabel;
	JLabel lblOutput_1;
	JLabel lblnput;
	TextArea lblWarningLang;
	TextArea lblWarningIoco;

	// TS Generation
	JButton btnGenerate, btnViewModel_gen, btnViewImplementation_gen;
	JLabel lblModel;
	JLabel lblInputLabel_gen;
	JLabel lblInput_gen, lblM;
	JLabel lblmodel_gen, lblLabel_gen, lblLabel_g;
	JLabel lblIut, lblimplementation_gen, lblOutput_gen, lblLabelOutput, imgModel_gen, imgImplementation_gen;
	JTextArea taTestCases_gen, taWarning_gen;

	JButton btnSaveTP;
	List<String> testSuite;
	Automaton_ multigraph;
	JLabel lblNumTC;

	private String pathImplementation = null;
	private String pathSpecification = null;
	private String failPath;
	JFileChooser fc = new JFileChooser();
	private JTextField tfInput;
	private JTextField tfOutput;
	// private final ButtonGroup buttonGroup = new ButtonGroup();
	JLabel lblInputIoco;
	JLabel lblOutputIoco;
	JTextArea taTestCasesIoco;
	JPanel panel_conf;
	JPanel panel_test_generation;
	JPanel panel_test_execution;

	private SystemColor backgroundColor = SystemColor.menu;
	private SystemColor labelColor = SystemColor.windowBorder;
	private SystemColor tipColor = SystemColor.windowBorder;
	private SystemColor borderColor = SystemColor.windowBorder;
	private SystemColor textColor = SystemColor.controlShadow;
	private SystemColor buttonColor = SystemColor.activeCaptionBorder;

	private Long lastModifiedSpec;
	private Long lastModifiedImp;
	List<String> words;
	Automaton_ multgraph;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConformanceView frame = new ConformanceView();
					frame.setResizable(false);
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
		lbl_veredict_lang.setText("");

		taTestCasesIoco.setText("");
		taTestCasesLang.setText("");

		btnSaveTP.setVisible(false);
		taTestCases_gen.setText("");

		lblNumTC.setVisible(false);
		

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
			lblImplementationIoco.setText(tfImplementation.getText());
			lblimplementationLang.setText(tfImplementation.getText());
			lblimplementation_gen.setText(tfImplementation.getText());
			// processModels(true, ioco);
			isImplementationProcess = false;

			lastModifiedImp = new File(pathImplementation).lastModified();

			closeFrame(true);

		} catch (Exception e) {

		}
	}

	public void closeFrame(boolean implementation) {
		// Frame[] allFrames = Frame.getFrames();
		for (Frame frame : Frame.getFrames()) {
			if (implementation) {
				if (frame.getTitle().startsWith(ViewConstants.titleFrameImgImplementation)) {
					showImplementationImage = true;
					frame.setVisible(false);
					frame.dispose();
					// enableShowImage(implementation);
				}
			} else {
				if (frame.getTitle().startsWith(ViewConstants.titleFrameImgSpecification)) {
					showSpecificationImage = true;
					frame.setVisible(false);
					frame.dispose();
				}
			}

		}
		// allFrames = null;
	}

	public void getSpecificationPath() {

		failPath = "";
		cleanVeredict();
		try {
			configFilterFile();
			fc.showOpenDialog(ConformanceView.this);
			tfSpecification.setText(fc.getSelectedFile().getName());
			pathSpecification = fc.getSelectedFile().getAbsolutePath();
			lblmodelIoco.setText(tfSpecification.getText());
			lblmodelLang.setText(tfSpecification.getText());
			lblmodel_gen.setText(tfSpecification.getText());
			isModelProcess = false;

			lastModifiedSpec = new File(pathSpecification).lastModified();
			closeFrame(false);

		} catch (Exception e) {
		}

	}

	public boolean regexIsValid(String exp) {
		try {
			// RegExp regExp = new RegExp(exp);
			// regExp.toAutomaton();
			// regExp = null;
			// RegExp regExp = new RegExp(exp);
			new RegExp(exp).toAutomaton();
			// regExp = null;
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public void verifyModelFileChange(boolean ioco, boolean bothModels) {

		if (pathSpecification != null) {
			if (!isModelProcess || lastModifiedSpec != new File(pathSpecification).lastModified()) {
				processModels(false, ioco);
				closeFrame(false);
				lastModifiedSpec = new File(pathSpecification).lastModified();
				showSpecificationImage = true;
			}
		}

		if (pathImplementation != null && bothModels) {
			if (!isImplementationProcess || lastModifiedImp != new File(pathImplementation).lastModified()) {
				processModels(true, ioco);
				closeFrame(true);
				lastModifiedImp = new File(pathImplementation).lastModified();
				showImplementationImage = true;

			}
		}

		try {
			cleanVeredict();
		} catch (Exception e) {

		}

	}

	public void showVeredict(boolean ioco) {
		if (conformidade != null) {// verified compliance
			if (S.getTransitions().size() > 0 || I.getTransitions().size() > 0) {
				if (ioco) {
					// System.out.println("|"+failPath+"|");
					if (!failPath.equals("")) {
						lbl_veredict_ioco.setText(Constants.MSG_NOT_CONFORM);
					} else {
						lbl_veredict_ioco.setText(Constants.MSG_CONFORM);
					}

					if (lbl_veredict_ioco.getText().equals(Constants.MSG_CONFORM)) {
						lbl_veredict_ioco.setForeground(new Color(0, 128, 0));
					}
					if (lbl_veredict_ioco.getText().equals(Constants.MSG_NOT_CONFORM) && !failPath.equals("")) {
						lbl_veredict_ioco.setForeground(new Color(178, 34, 34));
					} else {
						if (lbl_veredict_ioco.getText().equals(Constants.MSG_NOT_CONFORM) && failPath.equals("")) {
							lbl_veredict_ioco.setForeground(new Color(0, 128, 0));
							lbl_veredict_ioco.setText(Constants.MSG_CONFORM);
						}
					}
					// lbl_veredict_ioco.setVisible(true);

					// if (!failPath.equals("")) {
					taTestCasesIoco.setText(failPath);
					// btnTestCases_ioco.setVisible(true);
					// }
				} else {
					// lbl_veredict_lang.setText(Operations.veredict(conformidade));

					if (!failPath.equals("")) {
						lbl_veredict_lang.setText(Constants.MSG_NOT_CONFORM);
					} else {
						lbl_veredict_lang.setText(Constants.MSG_CONFORM);
					}

					if (lbl_veredict_lang.getText().equals(Constants.MSG_CONFORM)) {
						lbl_veredict_lang.setForeground(new Color(0, 128, 0));
					}
					if (lbl_veredict_lang.getText().equals(Constants.MSG_NOT_CONFORM) && !failPath.equals("")) {
						lbl_veredict_lang.setForeground(new Color(178, 34, 34));
					} else {
						if (lbl_veredict_lang.getText().equals(Constants.MSG_NOT_CONFORM) && failPath.equals("")) {
							lbl_veredict_lang.setForeground(new Color(0, 128, 0));
							lbl_veredict_lang.setText(Constants.MSG_CONFORM);
						}
					}

					if (!failPath.equals("")) {
						// btnTestCases_lang.setVisible(true);
						taTestCasesLang.setText(failPath);
					}
				}
			}
		}
	}

	final JPanel panel = new JPanel();

	public void actionVerifyConformance(boolean ioco) {
		/*
		 * lbl_veredict_ioco.setText("Processing...");
		 * lbl_veredict_ioco.setVisible(true);
		 */

		verifyModelFileChange(ioco, true);
		errorMessage(ioco);

		// IOLTS s = S;
		// IOLTS i = I;

		// verifyModelFileChange(ioco);

		if (isFormValid(ioco)) {// isFormValid(ioco)

			if (S != null && I != null) {

				JFrame loading = loadingDialog();
				loading.setVisible(true);
				if (ioco) {
					iocoConformance();
				} else {
					languageBasedConformance();
				}
				loading.dispose();

				System.gc();

				showVeredict(ioco);
				// btnVerifyConf_ioco.setText("Verify");
			}

			/*
			 * if (ioco) { lblWarningIoco.setText(""); } else { lblWarningLang.setText("");
			 * }
			 */

		} /*
			 * else { errorMessage(ioco); }
			 */

	}

	BufferedImage pathImageModel = null;
	BufferedImage pathImageImplementation = null;

	public void enableShowImage(boolean lts, boolean implementation) {

		try {
			if (((!tfImplementation.getText().isEmpty() && implementation)
					|| (!tfSpecification.getText().isEmpty() && !implementation))
					&& ((cbModel.getSelectedItem() == ViewConstants.IOLTS_CONST
							&& ((cbLabel.getSelectedItem() == ViewConstants.typeAutomaticLabel)
									|| (cbLabel.getSelectedItem() == ViewConstants.typeManualLabel
											&& !tfInput.getText().isEmpty() && !tfOutput.getText().isEmpty()))))
					|| lts) {
				setModel(lts, implementation);
				if (implementation && I.getTransitions().size() > 0) {
					pathImageImplementation = ModelImageGenerator.generateImage(I);
					closeFrame(implementation);
					if (pathImageImplementation != null) {
						btnViewImplementationIoco.setVisible(true);
						btnViewImplementationLang.setVisible(true);
						btnViewImplementationIoco.setEnabled(true);
						btnViewImplementationLang.setEnabled(true);
					} else {
						btnViewImplementationIoco.setVisible(true);
						btnViewImplementationLang.setVisible(true);
						btnViewImplementationIoco.setEnabled(false);
						btnViewImplementationLang.setEnabled(false);
					}

				} else {
					if (S.getTransitions().size() > 0) {
						pathImageModel = ModelImageGenerator.generateImage(S);
						closeFrame(implementation);
						if (pathImageModel != null) {
							btnViewModelIoco.setVisible(true);
							btnViewModelLang.setVisible(true);
							btnViewModelIoco.setEnabled(true);
							btnViewModelLang.setEnabled(true);
						} else {
							btnViewModelIoco.setVisible(true);
							btnViewModelLang.setVisible(true);
							btnViewModelIoco.setEnabled(false);
							btnViewModelLang.setEnabled(false);
						}
					}

				}
			} else {
				if (implementation) {
					btnViewImplementationIoco.setVisible(true);
					btnViewImplementationLang.setVisible(true);
					btnViewImplementationIoco.setEnabled(false);
					btnViewImplementationLang.setEnabled(false);

					isImplementationProcess = false;
				} else {
					btnViewModelIoco.setVisible(true);
					btnViewModelLang.setVisible(true);
					btnViewModelIoco.setEnabled(false);
					btnViewModelLang.setEnabled(false);

					isModelProcess = false;
				}
			}
		} catch (Exception ex) {

		}

	}

	public void setModel(boolean lts, boolean implementation) {

		try {
			// LTS S_ = new LTS(), I_ = new LTS();

			if (lts) {
				if (!implementation) {
					// S_ = ImportAutFile_WithoutThread.autToLTS(pathSpecification);
					// tfInput.setText(StringUtils.join(S_.getAlphabet(), ","));

					tfInput.setText(
							StringUtils.join(ImportAutFile.autToLTS(pathSpecification, false).getAlphabet(), ","));

				} else {
					// I_ = ImportAutFile_WithoutThread.autToLTS(pathImplementation);
					// tfInput.setText(StringUtils.join(I_.getAlphabet(), ","));

					tfInput.setText(
							StringUtils.join(ImportAutFile.autToLTS(pathImplementation, false).getAlphabet(), ","));
				}

				// S_ = null;
				// I_= null;

			}

			if (cbLabel.getSelectedIndex() == 2 || lts) {// manual input/output
				ArrayList<String> inp = new ArrayList<>(Arrays.asList(tfInput.getText().split(",")));
				ArrayList<String> out = new ArrayList<>(Arrays.asList(tfOutput.getText().split(",")));

				// remove space after/before alphabet on tfInput/tfOutput
				for (int i = 0; i < inp.size(); i++) {
					inp.set(i, inp.get(i).trim());
				}

				for (int i = 0; i < out.size(); i++) {
					out.set(i, out.get(i).trim());
				}

				if (!implementation) {
					S = ImportAutFile.autToIOLTS(pathSpecification, true, inp, out);
				} else {
					I = ImportAutFile.autToIOLTS(pathImplementation, true, inp, out);
				}

				if (lts) {
					tfInput.setText("");
				}

				out = null;
				inp = null;
				// lblWarningIoco.setText("");
				// lblWarningLang.setText("");
			} else {// ?/!
				if (!implementation) {
					S = ImportAutFile.autToIOLTS(pathSpecification, false, new ArrayList<String>(),
							new ArrayList<String>());

				} else {
					I = ImportAutFile.autToIOLTS(pathImplementation, false, new ArrayList<String>(),
							new ArrayList<String>());

				}

				boolean msg = false;

				if (S != null) {
					if (S.getTransitions().size() == 0) {
						msg = true;
					}
				}

				if (I != null) {
					if (I.getTransitions().size() == 0) {
						msg = true;
					}
				}

				if (msg) {
					if (!lblWarningIoco.getText().contains(ViewConstants.msgImp)) {
						lblWarningIoco.setText(lblWarningIoco.getText() + ViewConstants.msgImp);
					}

					if (!lblWarningLang.getText().contains(ViewConstants.msgImp)) {
						lblWarningLang.setText(lblWarningLang.getText() + ViewConstants.msgImp);
					}

				} else {
					removeMessage(true, ViewConstants.msgImp);
					removeMessage(false, ViewConstants.msgImp);
				}

			}

			if (I == null) {
				isImplementationProcess = false;
			} else {
				// IUT with quiescent transitions
				if (implementation) {
					I.addQuiescentTransitions();
				}
			}

			if (S == null) {
				isModelProcess = false;
			} else {
				if (!implementation) {
					S.addQuiescentTransitions();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (implementation) {
				isImplementationProcess = false;
				I = null;
			} else {
				isModelProcess = false;
				S = null;
			}

		}

	}

	public void standardizeLabelsIOLTS(boolean implementation) {
		// List<String> alphabet = new ArrayList();
		// HashSet hashSet_s_;
		if (implementation) {
			// alphabet.addAll(I.getAlphabet());
			// hashSet_s_ = new LinkedHashSet<>(alphabet);
			// alphabet = new ArrayList<>(hashSet_s_);
			// I.setAlphabet(alphabet);
			I.setAlphabet(new ArrayList<>(new LinkedHashSet<>(I.getAlphabet())));

			// alphabet = new ArrayList();
			// alphabet.addAll(I.getInputs());
			// hashSet_s_ = new LinkedHashSet<>(alphabet);
			// alphabet = new ArrayList<>(hashSet_s_);
			// I.setInputs(alphabet);
			I.setInputs(new ArrayList<>(new LinkedHashSet<>(I.getInputs())));

			// alphabet = new ArrayList();
			// alphabet.addAll(I.getOutputs());
			// hashSet_s_ = new LinkedHashSet<>(alphabet);
			// alphabet = new ArrayList<>(hashSet_s_);
			// I.setOutputs(alphabet);
			I.setInputs(new ArrayList<>(new LinkedHashSet<>(I.getOutputs())));

		} else {
			// alphabet.addAll(S.getAlphabet());
			// hashSet_s_ = new LinkedHashSet<>(alphabet);
			// alphabet = new ArrayList<>(hashSet_s_);
			// S.setAlphabet(alphabet);
			//
			// alphabet = new ArrayList();
			// alphabet.addAll(S.getInputs());
			// hashSet_s_ = new LinkedHashSet<>(alphabet);
			// alphabet = new ArrayList<>(hashSet_s_);
			// S.setInputs(alphabet);
			//
			// alphabet = new ArrayList();
			// alphabet.addAll(S.getOutputs());
			// hashSet_s_ = new LinkedHashSet<>(alphabet);
			// alphabet = new ArrayList<>(hashSet_s_);
			// S.setOutputs(alphabet);

			S.setAlphabet(new ArrayList<>(new LinkedHashSet<>(S.getAlphabet())));
			S.setInputs(new ArrayList<>(new LinkedHashSet<>(S.getInputs())));
			S.setInputs(new ArrayList<>(new LinkedHashSet<>(S.getOutputs())));
		}

		// alphabet = null;
	}

	public void processModels(boolean implementation, boolean ioco) {

		boolean lts = false;

		if (cbModel.getSelectedIndex() == 0
				|| (cbLabel.getSelectedIndex() == 0 && cbModel.getSelectedItem() == ViewConstants.IOLTS_CONST)
				|| cbModel.getSelectedItem() == ViewConstants.LTS_CONST
				|| (cbLabel.getSelectedItem() == ViewConstants.typeManualLabel && tfInput.getText().isEmpty()
						&& tfOutput.getText().isEmpty())) {
			lts = true;
		}

		if (!implementation) {
			isModelProcess = true;
		} else {
			isImplementationProcess = true;
		}

		enableShowImage(lts, implementation);

		try {
			if ((ioco && isFormValid(ioco)) || (!ioco && isFormValid(ioco))) {

				if (lts) {
					showModelLabel_(true);
				} else {
					showModelLabel_(false);
				}

			} else {
				if (lts) {
					showModelLabel(true);
				} else {
					showModelLabel(false);
				}
			}

		} catch (Exception e) {

		}

	}

	public void showModelLabel(boolean label) {
		lblLabel.setVisible(label);
		lblLabelIoco.setVisible(label);
		lblLabel_.setVisible(label);
		lblLabelLang.setVisible(label);
		lblLabel_gen.setVisible(label);
		lblLabel_gen.setVisible(label);

		lblnput.setVisible(!label);
		lblOutput_.setVisible(!label);
		lblInput_.setVisible(!label);
		lblOutput_1.setVisible(!label);
		// lblLabelInp.setVisible(!label);
		// lblLabelOut.setVisible(!label);

		lblInputIoco.setVisible(!label);
		lblOutputIoco.setVisible(!label);
		lblInputLang.setVisible(!label);
		lblOutputLang.setVisible(!label);
		lblInput_gen.setVisible(!label);
		lblOutput_gen.setVisible(!label);
	}

	public void showModelLabel_(boolean lts) {
		List<String> a = new ArrayList<>();
		// LinkedHashSet hashSet_s;

		if (lts) {
			showModelLabel(true);

			if (S != null) {
				a.addAll(S.getAlphabet());
			}

			if (I != null) {
				a.addAll(I.getAlphabet());
			}

			// hashSet_s = new LinkedHashSet<>(a);
			a = new ArrayList<>(new LinkedHashSet<>(a));

			lblLabelIoco.setText(StringUtils.join(a, ","));
			lblLabelLang.setText(StringUtils.join(a, ","));
		} else {
			if (S != null) {
				a.addAll(S.getInputs());
			}

			if (I != null) {
				a.addAll(I.getInputs());
			}

			// hashSet_s = new LinkedHashSet<>(a);
			a = new ArrayList<>(new LinkedHashSet<>(a));
			showModelLabel(false);
			lblInputIoco.setText(StringUtils.join(a, ","));
			lblInputLang.setText(StringUtils.join(a, ","));

			lblInput_gen.setText(StringUtils.join(a, ","));

			a = new ArrayList<>();
			if (S != null) {
				a.addAll(S.getOutputs());
			}
			if (I != null) {
				a.addAll(I.getOutputs());
			}
			// hashSet_s = new LinkedHashSet<>(a);
			a = new ArrayList<>(new LinkedHashSet<>(a));
			lblOutputIoco.setText(StringUtils.join(a, ","));
			lblOutputLang.setText(StringUtils.join(a, ","));
			lblOutput_gen.setText(StringUtils.join(a, ","));

		}

		a = null;
	}

	boolean isModelProcess = false;
	boolean isImplementationProcess = false;

	private JFrame loadingDialog() {
		JFrame jframe = new JFrame("Processing...");
		// JLabel lblLoading = new JLabel("Processing ...");
		// lblLoading.setFont(new Font("Tahoma", Font.PLAIN, 15));
		// lblLoading.setBounds(186, 11, 89, 26);
		// lblLoading.setVisible(true);
		// JProgressBar progressBar = new JProgressBar();
		// progressBar.setIndeterminate(true);
		// progressBar.setBounds(22, 36, 389, 14);
		// progressBar.setVisible(true);
		//
		//
		// JPanel contentPane = new JPanel();
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// contentPane.setLayout(null);
		// contentPane.add(lblLoading);
		// contentPane.add(progressBar);

		// jframe.setBounds(100, 100, 450, 105);
		int width = 350;
		int height = 105;

		jframe.setBounds(getX() + (getWidth() - width) / 2, getY() + (getHeight() - height) / 2 + 50, width, height);
		return jframe;

	}

	/**
	 * Create the frame.
	 */
	public ConformanceView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/img/icon.PNG")));
		setTitle(ViewConstants.toolName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 833, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {

				boolean ioco = false;
				boolean generation = false;

				String tab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());

				JFrame loading = loadingDialog();

				// TODO Auto-generated method stub
				if (tab.equals(ViewConstants.tabIOCO)) {
					ioco = true;

					// lbl_veredict_ioco.setVisible(false);
					lbl_veredict_ioco.setText("[Verdict]");
					lbl_veredict_ioco.setForeground(SystemColor.windowBorder);
					if (tfNTestCasesIOCO.getText().isEmpty()) {
						tfNTestCasesIOCO.setText(Objects.toString(Constants.MAX_TEST_CASES));
					}

					loading.setVisible(true);
				} else {
					if (tab.equals(ViewConstants.tabLang)) {
						ioco = false;

						if (tfNTestCasesLang.getText().isEmpty()) {
							tfNTestCasesLang.setText(Objects.toString(Constants.MAX_TEST_CASES));
						}
						loading.setVisible(true);
					} else {
						if (tab.equals(ViewConstants.tabTSGeneration)) {
							generation = true;
							verifyModelFileChange(ioco, false);

							errorMessageGen();

							if (isFormValidGeneration()) {
								showModelLabel_(false);
								verifyInpOutEmpty(false, true);
								verifyModelsEmpty(false, false);
							}
							removeMessageGen(ViewConstants.selectImplementation);
						}
					}
				}

				if (tab.equals(ViewConstants.tabIOCO) || tab.equals(ViewConstants.tabLang)) {
					verifyModelFileChange(ioco, true);
					try {
						if (!isFormValid(ioco)) {
							errorMessage(ioco);
						} else {
							errorMessage(ioco);// clean error message
							verifyInpOutEmpty(false, false);
							verifyModelsEmpty(false, true);

							/*
							 * if (ioco) { lblWarningIoco.setText(""); }
							 */

							/*
							 * if (!ioco) { //lblWarningLang.setText(""); verifyInpOutEmpty(false); }
							 */
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (loading != null) {
					loading.dispose();
				}

			}
		});

		tabbedPane.setBackground(backgroundColor);
		tabbedPane.setFont(new Font("Microsoft YaHei Light", Font.PLAIN, 13));
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		panel_conf = new JPanel();
		panel_conf.setForeground(SystemColor.textInactiveText);
		panel_conf.setBackground(backgroundColor);
		panel_conf.setToolTipText("");
		tabbedPane.addTab("Configuration", null, panel_conf, null);
		panel_conf.setLayout(null);

		cbModel = new JComboBox();
		cbModel.setForeground(textColor);
		cbModel.setBackground(backgroundColor);
		cbModel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				actionCbModel(arg0.getItem().toString());
			}
		});

		cbModel.setModel(new DefaultComboBoxModel(ViewConstants.models));
		cbModel.setFont(new Font("Dialog", Font.BOLD, 13));
		cbModel.setBounds(37, 167, 324, 26);
		cbModel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(cbModel);

		lblImplementation = new JLabel("Implementation");
		lblImplementation.setBackground(backgroundColor);
		lblImplementation.setForeground(labelColor);
		lblImplementation.setFont(new Font("Dialog", Font.BOLD, 13));
		lblImplementation.setBounds(37, 66, 157, 22);
		panel_conf.add(lblImplementation);

		lblSpecification = new JLabel("Model");
		lblSpecification.setForeground(SystemColor.controlDkShadow);
		lblSpecification.setFont(new Font("Dialog", Font.BOLD, 13));
		lblSpecification.setBounds(37, 11, 223, 14);
		panel_conf.add(lblSpecification);

		tfImplementation = new JTextField();
		tfImplementation.setForeground(textColor);
		tfImplementation.setBackground(backgroundColor);
		tfImplementation.setToolTipText("accepts only .aut files");
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
		tfImplementation.setBounds(37, 93, 700, 26);
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
		tfSpecification.setBounds(37, 29, 700, 26);
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
		btnFolderImp.setIcon(new ImageIcon(this.getClass().getResource(ViewConstants.folderIconPath)));
		btnFolderImp.setBounds(736, 93, 39, 28);
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
		btnFolderSpec.setIcon(new ImageIcon(this.getClass().getResource(ViewConstants.folderIconPath)));
		btnFolderSpec.setBounds(736, 29, 39, 28);
		panel_conf.add(btnFolderSpec);

		lblOutput = new JLabel("Output  labels");
		lblOutput.setForeground(labelColor);
		lblOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOutput.setBounds(37, 307, 274, 22);
		lblOutput.setVisible(false);
		panel_conf.add(lblOutput);

		lblInput = new JLabel("Input labels");
		lblInput.setForeground(labelColor);
		lblInput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblInput.setBounds(37, 227, 223, 22);
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
				isModelProcess = false;
				isImplementationProcess = false;

				if (!tfInput.getText().isEmpty()) {
					removeMessage(true, ViewConstants.selectInpOut);
					removeMessage(false, ViewConstants.selectInpOut);
				}
			}
		});
		tfInput.setToolTipText("");
		tfInput.setFont(new Font("Dialog", Font.BOLD, 13));
		tfInput.setColumns(10);
		tfInput.setBounds(37, 247, 738, 22);
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
				isModelProcess = false;
				isImplementationProcess = false;

				if (!tfInput.getText().isEmpty()) {
					removeMessage(true, ViewConstants.selectInpOut);
					removeMessage(false, ViewConstants.selectInpOut);
				}
			}
		});
		tfOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		tfOutput.setColumns(10);
		tfOutput.setBounds(37, 327, 738, 22);
		tfOutput.setVisible(false);
		tfOutput.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(tfOutput);

		lblLabelInp = new JLabel("(separated by comma)");
		lblLabelInp.setBackground(backgroundColor);
		lblLabelInp.setForeground(tipColor);
		lblLabelInp.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLabelInp.setBounds(651, 280, 173, 14);
		lblLabelInp.setVisible(false);
		panel_conf.add(lblLabelInp);

		lblLabelOut = new JLabel("(separated by comma)");
		lblLabelOut.setBackground(backgroundColor);
		lblLabelOut.setForeground(tipColor);
		lblLabelOut.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLabelOut.setBounds(651, 358, 164, 14);
		lblLabelOut.setVisible(false);
		panel_conf.add(lblLabelOut);

		JList list = new JList();
		list.setBounds(371, 25, 1, 1);
		panel_conf.add(list);

		lblRotulo = new JLabel("Label");
		lblRotulo.setForeground(labelColor);
		lblRotulo.setFont(new Font("Dialog", Font.BOLD, 13));
		lblRotulo.setBounds(451, 143, 274, 22);
		lblRotulo.setVisible(false);
		panel_conf.add(lblRotulo);

		cbLabel = new JComboBox();
		cbLabel.setVisible(false);
		cbLabel.setForeground(textColor);
		cbLabel.setBackground(backgroundColor);
		cbLabel.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				actionCbLabel(arg0.getItem().toString());
			}
		});
		cbLabel.setModel(new DefaultComboBoxModel(
				new String[] { "", ViewConstants.typeAutomaticLabel, ViewConstants.typeManualLabel }));
		cbLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		cbLabel.setBounds(451, 167, 324, 26);
		// cbLabel.setVisible(false);
		cbLabel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		panel_conf.add(cbLabel);

		JLabel lblIolts = new JLabel("Model type");
		lblIolts.setForeground(SystemColor.windowBorder);
		lblIolts.setFont(new Font("Dialog", Font.BOLD, 13));
		lblIolts.setBounds(37, 147, 144, 14);
		panel_conf.add(lblIolts);

		panel_ioco = new JPanel();
		tabbedPane.addTab(ViewConstants.tabIOCO, null, panel_ioco, null);
		panel_ioco.setLayout(null);

		tfNTestCasesIOCO = new JTextField();
		tfNTestCasesIOCO.setForeground(SystemColor.controlShadow);
		tfNTestCasesIOCO.setFont(new Font("Dialog", Font.BOLD, 13));
		tfNTestCasesIOCO.setBackground(UIManager.getColor("Button.background"));
		tfNTestCasesIOCO.setBounds(20, 142, 111, 32);
		tfNTestCasesIOCO.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfNTestCasesIOCO.setNextFocusableComponent(btnVerifyConf_ioco);
		panel_ioco.add(tfNTestCasesIOCO);
		tfNTestCasesIOCO.setColumns(10);

		btnVerifyConf_ioco = new JButton("Verify");

		btnVerifyConf_ioco.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				actionVerifyConformance(true);
			}
		});
		btnVerifyConf_ioco.setBounds(141, 130, 167, 44);
		btnVerifyConf_ioco.setFont(new Font("Dialog", Font.BOLD, 13));
		btnVerifyConf_ioco.setBackground(Color.LIGHT_GRAY);
		btnVerifyConf_ioco.setNextFocusableComponent(taTestCasesIoco);
		panel_ioco.add(btnVerifyConf_ioco);

		taTestCasesIoco = new JTextArea();
		taTestCasesIoco.setBounds(10, 277, 231, 150);
		JScrollPane scrolltxt = new JScrollPane(taTestCasesIoco);
		scrolltxt.setBounds(10, 201, 405, 247);
		panel_ioco.add(scrolltxt);

		lbl_veredict_ioco = new JLabel("[Verdict]");
		lbl_veredict_ioco.setForeground(SystemColor.windowBorder);
		lbl_veredict_ioco.setFont(new Font("Dialog", Font.BOLD, 13));
		lbl_veredict_ioco.setBounds(318, 142, 474, 20);
		panel_ioco.add(lbl_veredict_ioco);

		JLabel lblModel = new JLabel("Model");
		lblModel.setForeground(SystemColor.windowBorder);
		lblModel.setFont(new Font("Dialog", Font.BOLD, 13));
		lblModel.setBounds(37, 11, 52, 14);
		panel_ioco.add(lblModel);

		JLabel lblImplementation_1 = new JLabel("Implementation");
		lblImplementation_1.setForeground(SystemColor.windowBorder);
		lblImplementation_1.setFont(new Font("Dialog", Font.BOLD, 13));
		lblImplementation_1.setBounds(425, 11, 133, 22);
		panel_ioco.add(lblImplementation_1);

		lblnput = new JLabel("Input label");
		lblnput.setForeground(SystemColor.windowBorder);
		lblnput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblnput.setBounds(37, 71, 140, 14);
		panel_ioco.add(lblnput);

		lblOutput_1 = new JLabel("Output label");
		lblOutput_1.setForeground(SystemColor.windowBorder);
		lblOutput_1.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOutput_1.setBounds(425, 71, 149, 14);
		panel_ioco.add(lblOutput_1);

		lblmodelIoco = new JLabel("");
		lblmodelIoco.setForeground(SystemColor.controlShadow);
		lblmodelIoco.setBounds(37, 29, 265, 26);
		panel_ioco.add(lblmodelIoco);

		lblImplementationIoco = new JLabel("");
		lblImplementationIoco.setForeground(SystemColor.controlShadow);
		lblImplementationIoco.setBounds(425, 34, 367, 26);
		panel_ioco.add(lblImplementationIoco);

		lblInputIoco = new JLabel("");
		lblInputIoco.setForeground(SystemColor.controlShadow);
		lblInputIoco.setBounds(37, 88, 378, 26);
		panel_ioco.add(lblInputIoco);

		lblOutputIoco = new JLabel("");
		lblOutputIoco.setForeground(SystemColor.controlShadow);
		lblOutputIoco.setBounds(425, 88, 367, 26);
		panel_ioco.add(lblOutputIoco);

		imgModelIoco = new JLabel("");
		imgModelIoco.setVisible(false);
		imgModelIoco.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				// showModelImage(false);
			}
		});
		imgModelIoco.setBounds(469, 130, 44, 44);
		imgImplementationIoco = new JLabel("");
		imgImplementationIoco.setVisible(false);
		// imageShowHide(false, true);
		panel_ioco.add(imgModelIoco);

		imgImplementationIoco.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// showModelImage(true);
			}
		});
		imgImplementationIoco.setBounds(530, 130, 44, 44);

		panel_ioco.add(imgImplementationIoco);

		btnViewModelIoco = new JButton("View model");
		btnViewModelIoco.setVisible(false);
		btnViewModelIoco.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (showSpecificationImage && btnViewModelIoco.isEnabled()) {
					showModelImage(false);
				}
				if (!failPath.equals("")) {
					taTestCasesIoco.setText(failPath);
				}
				showVeredict(true);

			}
		});
		btnViewModelIoco.setFont(new Font("Dialog", Font.BOLD, 13));
		btnViewModelIoco.setBackground(Color.LIGHT_GRAY);
		btnViewModelIoco.setBounds(113, 5, 154, 26);
		panel_ioco.add(btnViewModelIoco);

		btnViewImplementationIoco = new JButton("View IUT");
		btnViewImplementationIoco.setVerticalAlignment(SwingConstants.BOTTOM);
		btnViewImplementationIoco.setNextFocusableComponent(tfNTestCasesIOCO);
		btnViewImplementationIoco.setVisible(false);
		btnViewImplementationIoco.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (showImplementationImage && btnViewImplementationIoco.isEnabled()) {
					showModelImage(true);
				}

				if (!failPath.equals("")) {
					taTestCasesIoco.setText(failPath);
				}

				showVeredict(true);
			}
		});
		btnViewImplementationIoco.setFont(new Font("Dialog", Font.BOLD, 13));
		btnViewImplementationIoco.setBackground(Color.LIGHT_GRAY);
		btnViewImplementationIoco.setBounds(568, 5, 154, 26);
		panel_ioco.add(btnViewImplementationIoco);

		lblLabel = new JLabel("Label");
		lblLabel.setVisible(false);
		lblLabel.setForeground(SystemColor.windowBorder);
		lblLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		lblLabel.setBounds(37, 71, 121, 14);
		panel_ioco.add(lblLabel);

		lblLabelIoco = new JLabel("");
		lblLabelIoco.setForeground(SystemColor.controlShadow);
		lblLabelIoco.setBounds(37, 88, 755, 26);
		panel_ioco.add(lblLabelIoco);

		JLabel lblWarnings = new JLabel("Warnings");
		lblWarnings.setForeground(SystemColor.windowBorder);
		lblWarnings.setFont(new Font("Dialog", Font.BOLD, 13));
		lblWarnings.setBounds(425, 179, 88, 14);
		panel_ioco.add(lblWarnings);

		lblWarningIoco = new TextArea("");
		lblWarningIoco.setForeground(SystemColor.controlShadow);
		lblWarningIoco.setBounds(425, 201, 367, 247);
		panel_ioco.add(lblWarningIoco);

		JLabel lblTestCases = new JLabel("# Test cases");
		lblTestCases.setForeground(SystemColor.windowBorder);
		lblTestCases.setFont(new Font("Dialog", Font.BOLD, 13));
		lblTestCases.setBounds(10, 125, 99, 14);
		panel_ioco.add(lblTestCases);

		/*
		 * taTestCasesLang = new JTextArea(); taTestCasesLang.setBounds(10, 282, 584,
		 * 197); panel_language.add(taTestCasesLang);
		 */

		// cleanVeredict();

		panel_language = new JPanel();
		tabbedPane.addTab(ViewConstants.tabLang, null, panel_language, null);
		panel_language.setLayout(null);

		lblD = new JLabel("Desirable behavior");
		lblD.setForeground(SystemColor.windowBorder);
		lblD.setFont(new Font("Dialog", Font.BOLD, 13));
		lblD.setBounds(37, 125, 244, 20);
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
		tfD.setBounds(37, 152, 352, 26);
		panel_language.add(tfD);

		lblRegexD = new JLabel("Regex example: (a|b)*c");
		lblRegexD.setForeground(SystemColor.windowBorder);
		lblRegexD.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRegexD.setBounds(255, 179, 225, 36);
		panel_language.add(lblRegexD);

		lblF = new JLabel("Undesirable behavior");
		lblF.setForeground(SystemColor.windowBorder);
		lblF.setFont(new Font("Dialog", Font.BOLD, 13));
		lblF.setBounds(425, 128, 200, 14);
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
		tfF.setBounds(425, 152, 367, 26);
		tfF.setNextFocusableComponent(tfNTestCasesLang);
		panel_language.add(tfF);

		lblRegexF = new JLabel("Regex example: (a|b)*c");
		lblRegexF.setForeground(SystemColor.windowBorder);
		lblRegexF.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRegexF.setBounds(652, 179, 184, 36);
		panel_language.add(lblRegexF);

		lbl_veredict_lang = new JLabel("");
		lbl_veredict_lang.setForeground(SystemColor.windowBorder);
		lbl_veredict_lang.setFont(new Font("Dialog", Font.BOLD, 13));
		lbl_veredict_lang.setBounds(318, 234, 474, 20);
		panel_language.add(lbl_veredict_lang);

		btnVerifyConf_lang = new JButton("Verify");
		btnVerifyConf_lang.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				actionVerifyConformance(false);
			}
		});
		btnVerifyConf_lang.setFont(new Font("Dialog", Font.BOLD, 13));
		btnVerifyConf_lang.setBackground(Color.LIGHT_GRAY);
		btnVerifyConf_lang.setBounds(147, 226, 167, 44);
		panel_language.add(btnVerifyConf_lang);

		label_1 = new JLabel("Model");
		label_1.setForeground(SystemColor.windowBorder);
		label_1.setFont(new Font("Dialog", Font.BOLD, 13));
		label_1.setBounds(37, 11, 52, 14);
		panel_language.add(label_1);

		lblInput_ = new JLabel("Input label");
		lblInput_.setForeground(SystemColor.windowBorder);
		lblInput_.setFont(new Font("Dialog", Font.BOLD, 13));
		lblInput_.setBounds(37, 71, 277, 14);
		panel_language.add(lblInput_);

		lblInputLang = new JLabel("");
		lblInputLang.setForeground(SystemColor.controlShadow);
		lblInputLang.setBounds(37, 91, 378, 26);
		panel_language.add(lblInputLang);

		lblmodelLang = new JLabel("");
		lblmodelLang.setForeground(SystemColor.controlShadow);
		lblmodelLang.setBounds(37, 29, 352, 26);
		panel_language.add(lblmodelLang);

		label_5 = new JLabel("Implementation");
		label_5.setForeground(SystemColor.windowBorder);
		label_5.setFont(new Font("Dialog", Font.BOLD, 13));
		label_5.setBounds(425, 11, 133, 22);
		panel_language.add(label_5);

		lblimplementationLang = new JLabel("");
		lblimplementationLang.setForeground(SystemColor.controlShadow);
		lblimplementationLang.setBounds(425, 29, 367, 26);
		panel_language.add(lblimplementationLang);

		lblOutput_ = new JLabel("Output label");
		lblOutput_.setForeground(SystemColor.windowBorder);
		lblOutput_.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOutput_.setBounds(425, 71, 167, 14);
		panel_language.add(lblOutput_);

		lblOutputLang = new JLabel("");
		lblOutputLang.setForeground(SystemColor.controlShadow);
		lblOutputLang.setBounds(425, 88, 367, 26);
		panel_language.add(lblOutputLang);

		taTestCasesLang = new JTextArea();
		taTestCasesLang.setEditable(false);
		taTestCasesLang.setBounds(10, 282, 584, 197);
		// taTestCasesLang.enable(false);
		JScrollPane scrolltxt2 = new JScrollPane(taTestCasesLang);
		scrolltxt2.setBounds(11, 293, 405, 160);

		panel_language.add(scrolltxt2);

		imgModelLang = new JLabel("");
		imgModelLang.setVisible(false);
		imgModelLang.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// showModelImage(false);
			}
		});
		imgModelLang.setBounds(678, 235, 44, 36);
		imgImplementationLang = new JLabel("");
		imgImplementationLang.setVisible(false);
		// imageShowHide(false, false);
		panel_language.add(imgModelLang);
		imgImplementationLang.setVisible(false);
		imgImplementationLang.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// showModelImage(true);
			}
		});
		imgImplementationLang.setBounds(660, 235, 44, 36);
		panel_language.add(imgImplementationLang);

		btnViewModelLang = new JButton("View model");
		btnViewModelLang.setVisible(false);
		btnViewModelLang.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (showSpecificationImage && btnViewModelLang.isEnabled()) {
					showModelImage(false);
				}
				if (!failPath.equals("")) {
					taTestCasesLang.setText(failPath);
				}
				showVeredict(false);
			}
		});
		btnViewModelLang.setFont(new Font("Dialog", Font.BOLD, 13));
		btnViewModelLang.setBackground(Color.LIGHT_GRAY);
		btnViewModelLang.setBounds(113, 5, 154, 26);
		panel_language.add(btnViewModelLang);

		btnViewImplementationLang = new JButton("View IUT");
		btnViewImplementationLang.setVisible(false);
		btnViewImplementationLang.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (showImplementationImage && btnViewImplementationLang.isEnabled()) {
					showModelImage(true);
				}
				if (!failPath.equals("")) {
					taTestCasesLang.setText(failPath);
				}
				showVeredict(false);
			}
		});
		btnViewImplementationLang.setFont(new Font("Dialog", Font.BOLD, 13));
		btnViewImplementationLang.setBackground(Color.LIGHT_GRAY);
		btnViewImplementationLang.setBounds(568, 5, 154, 26);
		panel_language.add(btnViewImplementationLang);

		lblLabelLang = new JLabel("");
		lblLabelLang.setForeground(SystemColor.controlShadow);
		lblLabelLang.setBounds(37, 88, 755, 26);
		panel_language.add(lblLabelLang);

		lblLabel_ = new JLabel("label");
		lblLabel_.setVisible(false);
		lblLabel_.setForeground(SystemColor.windowBorder);
		lblLabel_.setFont(new Font("Dialog", Font.BOLD, 13));
		lblLabel_.setBounds(37, 71, 106, 14);
		panel_language.add(lblLabel_);

		JLabel label = new JLabel("Warnings");
		label.setForeground(SystemColor.windowBorder);
		label.setFont(new Font("Dialog", Font.BOLD, 13));
		label.setBounds(426, 276, 93, 14);
		panel_language.add(label);

		lblWarningLang = new TextArea("");
		lblWarningLang.setForeground(SystemColor.controlShadow);
		lblWarningLang.setBounds(426, 293, 366, 160);
		panel_language.add(lblWarningLang);

		label_2 = new JLabel("# Test cases");
		label_2.setForeground(SystemColor.windowBorder);
		label_2.setFont(new Font("Dialog", Font.BOLD, 13));
		label_2.setBounds(11, 226, 99, 14);
		panel_language.add(label_2);

		tfNTestCasesLang = new JTextField();
		tfNTestCasesLang.setForeground(SystemColor.controlShadow);
		tfNTestCasesLang.setFont(new Font("Dialog", Font.BOLD, 13));
		tfNTestCasesLang.setColumns(10);
		tfNTestCasesLang.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfNTestCasesLang.setBackground(SystemColor.menu);
		tfNTestCasesLang.setBounds(21, 238, 111, 32);
		tfNTestCasesLang.setNextFocusableComponent(btnVerifyConf_lang);
		panel_language.add(tfNTestCasesLang);

		// Panel generation
		panel_test_generation = new JPanel();
		tabbedPane.addTab(ViewConstants.tabTSGeneration, null, panel_test_generation, null);
		panel_test_generation.setLayout(null);

		btnGenerate = new JButton("Generate");
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (isFormValidGeneration()) {
					JFrame loading = null;
					try {
						loading = loadingDialog();
						loading.setVisible(true);
						// System.out.println(S);
						removeMessageGen(ViewConstants.mInteger);

						multigraph = TestGeneration.multiGraphD(S, Integer.parseInt(tfM.getText()));
						// System.out.println(multgraph);
						testSuite = Graph.getWords(multigraph);

						multgraph = TestGeneration.multiGraphD(S, Integer.parseInt(tfM.getText()));
						// System.out.println(multgraph);
						words = Graph.getWords(multgraph);

						words.sort(Comparator.comparing(String::length));

						// System.out.println(StringUtils.join(words, ","));

						taTestCases_gen.setText(StringUtils.join(testSuite, "\n"));

						btnSaveTP.setVisible(true);

						lblNumTC.setVisible(true);
						lblNumTC.setText("# Test cases: " + words.size());
						
					} catch (NumberFormatException e) {
						taWarning_gen.setText(taWarning_gen.getText() + ViewConstants.mInteger);
					} finally {
						if (loading != null)
							loading.dispose();
					}
				}
			}
		});
		btnGenerate.setBounds(141, 130, 167, 44);
		btnGenerate.setFont(new Font("Dialog", Font.BOLD, 13));
		btnGenerate.setBackground(Color.LIGHT_GRAY);
		panel_test_generation.add(btnGenerate);

		lblModel = new JLabel("Model");
		lblModel.setForeground(SystemColor.windowBorder);
		lblModel.setFont(new Font("Dialog", Font.BOLD, 13));
		lblModel.setBounds(37, 11, 52, 14);
		panel_test_generation.add(lblModel);

		lblInputLabel_gen = new JLabel("Input label");
		lblInputLabel_gen.setForeground(SystemColor.windowBorder);
		lblInputLabel_gen.setFont(new Font("Dialog", Font.BOLD, 13));
		lblInputLabel_gen.setBounds(37, 71, 277, 14);
		panel_test_generation.add(lblInputLabel_gen);

		lblInput_gen = new JLabel("");
		lblInput_gen.setForeground(SystemColor.controlShadow);
		lblInput_gen.setBounds(37, 91, 378, 26);
		panel_test_generation.add(lblInput_gen);

		lblmodel_gen = new JLabel("");
		lblmodel_gen.setForeground(SystemColor.controlShadow);
		lblmodel_gen.setBounds(37, 29, 352, 26);
		panel_test_generation.add(lblmodel_gen);

		lblIut = new JLabel("Implementation");
		lblIut.setForeground(SystemColor.windowBorder);
		lblIut.setFont(new Font("Dialog", Font.BOLD, 13));
		lblIut.setBounds(425, 11, 133, 22);
		// panel_test_generation.add(lblIut);

		lblimplementation_gen = new JLabel("");
		lblimplementation_gen.setForeground(SystemColor.controlShadow);
		lblimplementation_gen.setBounds(425, 29, 367, 26);
		// panel_test_generation.add(lblimplementation_gen);

		lblLabelOutput = new JLabel("Output label");
		lblLabelOutput.setForeground(SystemColor.windowBorder);
		lblLabelOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblLabelOutput.setBounds(425, 71, 167, 14);
		panel_test_generation.add(lblLabelOutput);

		lblOutput_gen = new JLabel("");
		lblOutput_gen.setForeground(SystemColor.controlShadow);
		lblOutput_gen.setBounds(425, 88, 367, 26);
		panel_test_generation.add(lblOutput_gen);

		taTestCases_gen = new JTextArea();
		taTestCases_gen.setBounds(10, 277, 231, 150);
		JScrollPane scrolltxt3 = new JScrollPane(taTestCases_gen);
		scrolltxt3.setBounds(10, 201, 405, 196);
		panel_test_generation.add(scrolltxt3);

		imgModel_gen = new JLabel("");
		imgModel_gen.setVisible(false);
		imgModel_gen.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showModelImage(false);
			}
		});
		imgModel_gen.setBounds(678, 235, 44, 36);
		imgImplementation_gen = new JLabel("");
		imgImplementation_gen.setVisible(false);
		// imageShowHide(false, false);
		panel_test_generation.add(imgModel_gen);
		imgImplementation_gen.setVisible(false);
		imgImplementation_gen.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showModelImage(true);
			}
		});
		imgImplementation_gen.setBounds(660, 235, 44, 36);
		panel_test_generation.add(imgImplementation_gen);

		btnViewModel_gen = new JButton("View model");
		btnViewModel_gen.setVisible(false);
		btnViewModel_gen.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (showSpecificationImage && btnViewModel_gen.isEnabled()) {
					showModelImage(false);
				}
				if (!failPath.equals("")) {
					taTestCases_gen.setText(failPath);
				}
				showVeredict(false);
			}
		});
		btnViewModel_gen.setFont(new Font("Dialog", Font.BOLD, 13));
		btnViewModel_gen.setBackground(Color.LIGHT_GRAY);
		btnViewModel_gen.setBounds(113, 5, 154, 26);
		panel_test_generation.add(btnViewModel_gen);

		btnViewImplementation_gen = new JButton("view IUT");
		btnViewImplementation_gen.setVisible(false);
		btnViewImplementation_gen.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (showImplementationImage && btnViewImplementation_gen.isEnabled()) {
					showModelImage(true);
				}
				if (!failPath.equals("")) {
					taTestCases_gen.setText(failPath);
				}
				showVeredict(false);
			}
		});
		btnViewImplementation_gen.setFont(new Font("Dialog", Font.BOLD, 13));
		btnViewImplementation_gen.setBackground(Color.LIGHT_GRAY);
		btnViewImplementation_gen.setBounds(568, 5, 154, 26);
		btnViewImplementation_gen.setVisible(false);
		// panel_test_generation.add(btnViewImplementation_gen);

		lblLabel_gen = new JLabel("");
		lblLabel_gen.setForeground(SystemColor.controlShadow);
		lblLabel_gen.setBounds(37, 88, 755, 26);
		panel_test_generation.add(lblLabel_gen);

		lblLabel_g = new JLabel("label");
		lblLabel_g.setVisible(false);
		lblLabel_g.setForeground(SystemColor.windowBorder);
		lblLabel_g.setFont(new Font("Dialog", Font.BOLD, 13));
		lblLabel_g.setBounds(37, 71, 106, 14);
		panel_test_generation.add(lblLabel_g);

		JLabel lblWarning = new JLabel("Warnings");
		lblWarning.setForeground(SystemColor.windowBorder);
		lblWarning.setFont(new Font("Dialog", Font.BOLD, 13));
		lblWarning.setBounds(425, 178, 93, 14);
		panel_test_generation.add(lblWarning);

		taWarning_gen = new JTextArea("");
		taWarning_gen.setForeground(SystemColor.controlShadow);
		taWarning_gen.setBounds(426, 201, 366, 247);
		panel_test_generation.add(taWarning_gen);

		lblM = new JLabel("# max IUT states");
		lblM.setForeground(SystemColor.windowBorder);
		lblM.setFont(new Font("Dialog", Font.BOLD, 13));
		lblM.setBounds(11, 125, 117, 14);
		panel_test_generation.add(lblM);

		tfM = new JTextField();
		tfM.setForeground(SystemColor.controlShadow);
		tfM.setFont(new Font("Dialog", Font.BOLD, 13));
		tfM.setColumns(10);
		tfM.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfM.setBackground(SystemColor.menu);
		tfM.setBounds(20, 142, 111, 32);
		tfM.setText("1");
		panel_test_generation.add(tfM);

		btnSaveTP = new JButton("Save test purpose");
		btnSaveTP.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFrame loading = loadingDialog();
				try {
					JFileChooser fc = directoryChooser();
					fc.showOpenDialog(ConformanceView.this);

					loading.setVisible(true);

					// fc.getSelectedFile().getName()
					String path = fc.getSelectedFile().getAbsolutePath();

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

					new File(path + "/Tp " + dateFormat.format(new Date())).mkdirs();
					path = path + "/Tp " + dateFormat.format(new Date());
					File file;
					BufferedWriter writer;
					int count = 0;
					for (String tc : testSuite) {
						file = new File(path, "tp" + count + ".aut");
						writer = new BufferedWriter(new FileWriter(file));
						writer.write(AutGenerator
								.ioltsToAut(TestGeneration.testPurpose(multigraph, tc, S.getOutputs(), S.getInputs())));
						writer.close();
						// System.out.println(TestGeneration.testPurpose(multigraph, tc, S.getOutputs(),
						// S.getInputs()));
						count++;

						// if (count == 4) {
						// break;
						// }

					}

				} catch (Exception e) {

				} finally {
					if (loading != null) {
						loading.dispose();
					}
				}
			}
		});
		btnSaveTP.setFont(new Font("Dialog", Font.BOLD, 13));
		btnSaveTP.setBackground(Color.LIGHT_GRAY);
		btnSaveTP.setBounds(248, 404, 167, 44);
		btnSaveTP.setVisible(false);
		panel_test_generation.add(btnSaveTP);

		lblNumTC = new JLabel("# Test cases: ");
		lblNumTC.setForeground(SystemColor.windowBorder);
		lblNumTC.setFont(new Font("Dialog", Font.BOLD, 13));
		lblNumTC.setBounds(318, 146, 287, 14);
		lblNumTC.setVisible(false);
		panel_test_generation.add(lblNumTC);

		// Panel run test
		panel_test_execution = new JPanel();
		tabbedPane.addTab(ViewConstants.tabTestRun, null, panel_test_execution, null);
		panel_test_execution.setLayout(null);

		JLabel lblSelectFolderContaining = new JLabel("Folder containing the test purposes");
		lblSelectFolderContaining.setForeground(SystemColor.windowBorder);
		lblSelectFolderContaining.setFont(new Font("Dialog", Font.BOLD, 13));
		lblSelectFolderContaining.setBounds(37, 11, 231, 14);
		panel_test_execution.add(lblSelectFolderContaining);

		JRadioButton rdbtnOneIut = new JRadioButton("An implementation");
		rdbtnOneIut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clearRadioButtonIut();
				lblOneIut.setVisible(true);
				tfOneIut.setVisible(true);
				btnOneIut.setVisible(true);
			}
		});
		rdbtnOneIut.setBounds(37, 95, 155, 23);
		rdbtnOneIut.setForeground(SystemColor.windowBorder);
		rdbtnOneIut.setFont(new Font("Dialog", Font.BOLD, 13));
		panel_test_execution.add(rdbtnOneIut);

		JLabel lblImplementation_2 = new JLabel("Run mode");
		lblImplementation_2.setForeground(SystemColor.windowBorder);
		lblImplementation_2.setFont(new Font("Dialog", Font.BOLD, 13));
		lblImplementation_2.setBounds(37, 66, 157, 22);
		panel_test_execution.add(lblImplementation_2);

		JRadioButton rdbtnInBatch = new JRadioButton("Implementations in batch");
		rdbtnInBatch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clearRadioButtonIut();
				tfFolderIut.setVisible(true);
				lblFolderIut.setVisible(true);
				btnFolderIut.setVisible(true);
			}
		});
		rdbtnInBatch.setForeground(SystemColor.windowBorder);
		rdbtnInBatch.setFont(new Font("Dialog", Font.BOLD, 13));
		rdbtnInBatch.setBounds(194, 95, 194, 23);
		panel_test_execution.add(rdbtnInBatch);

		ButtonGroup groupIut = new ButtonGroup();
		groupIut.add(rdbtnOneIut);
		groupIut.add(rdbtnInBatch);

		tfTpFolder = new JTextField();
		tfTpFolder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectTPsFolder();
			}
		});
		tfTpFolder.setToolTipText("accepts only .aut files");
		tfTpFolder.setForeground(SystemColor.controlShadow);
		tfTpFolder.setFont(new Font("Dialog", Font.BOLD, 13));
		tfTpFolder.setColumns(10);
		tfTpFolder.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfTpFolder.setBackground(SystemColor.menu);
		tfTpFolder.setBounds(37, 29, 700, 26);
		panel_test_execution.add(tfTpFolder);

		JButton button = new JButton("");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectTPsFolder();
			}
		});
		button.setIcon(new ImageIcon(this.getClass().getResource(ViewConstants.folderIconPath)));
		button.setOpaque(true);
		button.setBackground(SystemColor.activeCaptionBorder);
		button.setBounds(736, 29, 39, 28);
		panel_test_execution.add(button);

		lblOneIut = new JLabel("Implementation");
		lblOneIut.setForeground(SystemColor.windowBorder);
		lblOneIut.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOneIut.setBounds(37, 134, 231, 14);
		lblOneIut.setVisible(false);
		panel_test_execution.add(lblOneIut);

		lblFolderIut = new JLabel("Implementations folder");
		lblFolderIut.setForeground(SystemColor.windowBorder);
		lblFolderIut.setFont(new Font("Dialog", Font.BOLD, 13));
		lblFolderIut.setBounds(37, 201, 231, 14);
		lblFolderIut.setVisible(false);
		panel_test_execution.add(lblFolderIut);

		tfOneIut = new JTextField();
		tfOneIut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getOneIutPath();
			}
		});
		tfOneIut.setToolTipText("accepts only .aut files");
		tfOneIut.setForeground(SystemColor.controlShadow);
		tfOneIut.setFont(new Font("Dialog", Font.BOLD, 13));
		tfOneIut.setColumns(10);
		tfOneIut.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfOneIut.setBackground(SystemColor.menu);
		tfOneIut.setBounds(37, 154, 700, 26);
		tfOneIut.setVisible(false);
		panel_test_execution.add(tfOneIut);

		tfFolderIut = new JTextField();
		tfFolderIut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getIutFolder();

			}
		});
		tfFolderIut.setToolTipText("accepts only .aut files");
		tfFolderIut.setForeground(SystemColor.controlShadow);
		tfFolderIut.setFont(new Font("Dialog", Font.BOLD, 13));
		tfFolderIut.setColumns(10);
		tfFolderIut.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfFolderIut.setBackground(SystemColor.menu);
		tfFolderIut.setBounds(37, 216, 700, 26);
		tfFolderIut.setVisible(false);
		panel_test_execution.add(tfFolderIut);

		btnOneIut = new JButton("");
		btnOneIut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getOneIutPath();
			}
		});
		btnOneIut.setOpaque(true);
		btnOneIut.setBackground(SystemColor.activeCaptionBorder);
		btnOneIut.setBounds(736, 154, 39, 28);
		btnOneIut.setIcon(new ImageIcon(this.getClass().getResource(ViewConstants.folderIconPath)));
		btnOneIut.setVisible(false);
		panel_test_execution.add(btnOneIut);

		btnFolderIut = new JButton("");
		btnFolderIut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getIutFolder();
			}
		});
		btnFolderIut.setOpaque(true);
		btnFolderIut.setBackground(SystemColor.activeCaptionBorder);
		btnFolderIut.setBounds(736, 218, 39, 28);
		btnFolderIut.setIcon(new ImageIcon(this.getClass().getResource(ViewConstants.folderIconPath)));
		btnFolderIut.setVisible(false);
		panel_test_execution.add(btnFolderIut);

		btnRun = new JButton("Run");
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				TestGeneration.runTestWithTP(tpFolder, rdbtnOneIut.isSelected(), pathImplementation, iutFolder,
						saveFolderRun);
			}
		});
		btnRun.setFont(new Font("Dialog", Font.BOLD, 13));
		btnRun.setBackground(Color.LIGHT_GRAY);
		btnRun.setBounds(232, 347, 167, 44);
		panel_test_execution.add(btnRun);

		JLabel label_3 = new JLabel("Warnings");
		label_3.setForeground(SystemColor.windowBorder);
		label_3.setFont(new Font("Dialog", Font.BOLD, 13));
		label_3.setBounds(408, 268, 93, 23);
		panel_test_execution.add(label_3);

		JTextArea textArea = new JTextArea("");
		textArea.setForeground(SystemColor.controlShadow);
		textArea.setBounds(409, 291, 366, 157);
		panel_test_execution.add(textArea);

		lblPathToSave = new JLabel("Save verdicts on");
		lblPathToSave.setForeground(SystemColor.windowBorder);
		lblPathToSave.setFont(new Font("Dialog", Font.BOLD, 13));
		lblPathToSave.setBounds(37, 268, 167, 14);
		panel_test_execution.add(lblPathToSave);

		tfVerdictSavePath = new JTextField();
		tfVerdictSavePath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getSaveVerdictRun();
			}
		});
		tfVerdictSavePath.setToolTipText("accepts only .aut files");
		tfVerdictSavePath.setForeground(SystemColor.controlShadow);
		tfVerdictSavePath.setFont(new Font("Dialog", Font.BOLD, 13));
		tfVerdictSavePath.setColumns(10);
		tfVerdictSavePath.setBorder(new MatteBorder(0, 0, 1, 0, (Color) borderColor));
		tfVerdictSavePath.setBackground(SystemColor.menu);
		tfVerdictSavePath.setBounds(37, 291, 322, 26);
		panel_test_execution.add(tfVerdictSavePath);

		JButton button_1 = new JButton("");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getSaveVerdictRun();
			}
		});
		button_1.setOpaque(true);
		button_1.setBackground(SystemColor.activeCaptionBorder);
		button_1.setBounds(359, 289, 39, 28);
		button_1.setIcon(new ImageIcon(this.getClass().getResource(ViewConstants.folderIconPath)));
		panel_test_execution.add(button_1);

		clearRadioButtonIut();

	}

	public static boolean isAutFile(File f) {
		return (f.getName().indexOf(".") != -1 && f.getName().substring(f.getName().indexOf(".")).equals(".aut"));
	}

	String iutFolder;

	public void getIutFolder() {
		JFileChooser fc = directoryChooser();
		fc.showOpenDialog(ConformanceView.this);
		iutFolder = fc.getSelectedFile().getAbsolutePath();
		tfFolderIut.setText(fc.getSelectedFile().getName());
	}

	String saveFolderRun;

	public void getSaveVerdictRun() {
		JFileChooser fc = directoryChooser();
		fc.showOpenDialog(ConformanceView.this);
		saveFolderRun = fc.getSelectedFile().getAbsolutePath();
		tfVerdictSavePath.setText(fc.getSelectedFile().getName());
	}

	public void getOneIutPath() {

		try {
			configFilterFile();
			fc.showOpenDialog(ConformanceView.this);

			tfOneIut.setText(fc.getSelectedFile().getName());
			pathImplementation = fc.getSelectedFile().getAbsolutePath();
			fc.setCurrentDirectory(fc.getSelectedFile().getParentFile());

			lastModifiedImp = new File(pathImplementation).lastModified();

			closeFrame(true);

		} catch (Exception e) {

		}
	}

	public void clearRadioButtonIut() {
		// if (all || (oneIut != null && !oneIut)) {
		tfFolderIut.setVisible(false);
		lblFolderIut.setVisible(false);
		btnFolderIut.setVisible(false);
		// }
		// if (all || (oneIut != null && oneIut)) {
		tfOneIut.setVisible(false);
		lblOneIut.setVisible(false);
		btnOneIut.setVisible(false);
		// }
	}

	String tpFolder;

	public void selectTPsFolder() {
		JFileChooser fc = directoryChooser();
		fc.showOpenDialog(ConformanceView.this);
		tpFolder = fc.getSelectedFile().getAbsolutePath();
		tfTpFolder.setText(fc.getSelectedFile().getName());
	}

	public JFileChooser directoryChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		return fc;
	}

	boolean showImplementationImage = true;
	boolean showSpecificationImage = true;

	public void showModelImage(boolean implementation) {
		int size = 550;

		int width;
		int height;

		verifyModelFileChange(true, true);
		verifyModelFileChange(false, true);

		String model = (implementation) ? ViewConstants.titleFrameImgImplementation + tfImplementation.getText()
				: ViewConstants.titleFrameImgSpecification + tfSpecification.getText();

		try {
			JFrame frame = new JFrame(model);
			frame.setVisible(true);
			frame.setResizable(false);

			JPanel panel = new JPanel();

			JLabel jl = new JLabel();

			if (implementation) {
				// bimg = ImageIO.read(new File(pathImageImplementation));
				width = pathImageImplementation.getWidth();
				height = pathImageImplementation.getHeight();
				frame.setSize(width + 50, height + 50);

				jl.setIcon(new ImageIcon(new ImageIcon(pathImageImplementation).getImage().getScaledInstance(width,
						height, Image.SCALE_DEFAULT)));
				showImplementationImage = false;
			} else {
				// bimg = ImageIO.read(new File(pathImageModel));
				width = pathImageModel.getWidth();
				height = pathImageModel.getHeight();

				frame.setSize(width + 50, height + 50);
				jl.setIcon(new ImageIcon(new ImageIcon(pathImageModel).getImage().getScaledInstance(width, height,
						Image.SCALE_DEFAULT)));
				showSpecificationImage = false;
			}

			panel.add(jl);
			JScrollPane scrolltxt = new JScrollPane(panel);
			scrolltxt.setBounds(3, 3, width / (width % size), height / (height % size));
			// panel.add(scrolltxt);
			frame.getContentPane().add(scrolltxt);

			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				// when image closed
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					if (frame.getTitle().startsWith(ViewConstants.titleFrameImgImplementation)) {
						showImplementationImage = true;
					}

					if (frame.getTitle().startsWith(ViewConstants.titleFrameImgSpecification)) {
						showSpecificationImage = true;
					}

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

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
		if (label.equals(ViewConstants.typeManualLabel)) {
			setInputOutputField(true);
			removeMessage(true, ViewConstants.msgImp);
			removeMessage(false, ViewConstants.msgImp);
		} else {
			setInputOutputField(false);
		}
		isModelProcess = false;
		isImplementationProcess = false;
	}

	public void actionCbModel(String model) {
		if (model.equals(ViewConstants.IOLTS_CONST)) {
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
		isModelProcess = false;
		isImplementationProcess = false;
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
	IOLTS S, I = null;

	public void iocoConformance() {
		conformidade = null;

		if (S.getTransitions().size() != 0 || I.getTransitions().size() != 0) {
			failPath = "";
			conformidade = IocoConformance.verifyIOCOConformance(S, I, Integer.parseInt(tfNTestCasesIOCO.getText()));
			if (conformidade.getFinalStates().size() > 0) {
				failPath = Operations.path(S, I, conformidade, true, false,
						Integer.parseInt(tfNTestCasesIOCO.getText()));
			}
		}

	}

	public void languageBasedConformance() {
		boolean lts = false;
		conformidade = null;
		// IOLTS S, I = null;
		LTS S_, I_ = null;
		try {

			// when the model type is not selected or IOLTS is selected but not specified
			// how to differentiate the inputs and outputs
			if (cbModel.getSelectedIndex() == 0
					|| (cbLabel.getSelectedIndex() == 0 && cbModel.getSelectedItem() == ViewConstants.IOLTS_CONST)
					|| cbModel.getSelectedItem() == ViewConstants.LTS_CONST
					|| (cbLabel.getSelectedItem() == ViewConstants.typeManualLabel && tfInput.getText().isEmpty()
							&& tfOutput.getText().isEmpty())) {
				lts = true;
			}

			if (!lts) { // IOLTS

				S_ = S.toLTS();
				I_ = I.toLTS();

			} else {
				S_ = ImportAutFile.autToLTS(pathSpecification, false);
				I_ = ImportAutFile.autToLTS(pathImplementation, false);
			}

			if (S_.getAlphabet().size() != 0 || I_.getAlphabet().size() != 0) {
				String D = "";
				D = tfD.getText();
				if (tfD.getText().isEmpty() && tfF.getText().isEmpty()) {
					D = "(";
					List<String> alphabets = new ArrayList<>();
					alphabets.addAll(S_.getAlphabet());
					alphabets.addAll(I_.getAlphabet());
					for (String l : new ArrayList<>(new LinkedHashSet<>(alphabets))) {
						D += l + "|";
					}
					D = D.substring(0, D.length() - 1);
					D += ")*";

				}
				tfD.setText(D);
				String F = tfF.getText();

				if (regexIsValid(D) && regexIsValid(F)) {
					conformidade = LanguageBasedConformance.verifyLanguageConformance(S_, I_, D, F,
							Integer.parseInt(tfNTestCasesLang.getText()));
					if (conformidade.getFinalStates().size() > 0) {
						failPath = Operations.path(S_, I_, conformidade, false, false,
								Integer.parseInt(tfNTestCasesLang.getText()));
					} else {
						failPath = "";
					}
					removeMessage(false, ViewConstants.invalidRegex);
				} else {
					// JOptionPane.showMessageDialog(panel, "Invalid regex!", "Warning",
					// JOptionPane.WARNING_MESSAGE);
					if (!lblWarningLang.getText().contains(ViewConstants.invalidRegex)) {
						lblWarningLang.setText(lblWarningLang.getText() + ViewConstants.invalidRegex);
					}

					return;
				}
			}

			removeMessage(false, ViewConstants.exceptionMessage);
		} catch (Exception e_) {
			if (!lblWarningLang.getText().contains(ViewConstants.exceptionMessage)) {
				lblWarningLang.setText(lblWarningLang.getText() + ViewConstants.exceptionMessage);
			}

			// JOptionPane.showMessageDialog(panel, e_.getMessage(), "Warning",
			// JOptionPane.WARNING_MESSAGE);
			e_.printStackTrace();
			return;
		}
	}

	private JPanel panel_language;
	private JPanel panel_ioco;
	private JTextField tfD;
	private JTextField tfF;
	private JButton btnVerifyConf_ioco;
	private JLabel lbl_veredict_lang;
	private JLabel lbl_veredict_ioco;
	private JButton btnVerifyConf_lang;
	private JLabel label_1;
	private JLabel lblInput_;
	private JLabel lblInputLang;
	private JLabel lblmodelLang;
	private JLabel label_5;
	private JLabel lblimplementationLang;
	private JLabel lblOutput_;
	private JLabel lblOutputLang;
	private JTextArea taTestCasesLang;
	private JLabel imgModelIoco;
	private JLabel imgImplementationIoco;
	private JLabel imgModelLang;
	private JLabel imgImplementationLang;
	private JLabel lblLabelLang;
	private JLabel lblLabel_;
	private JTextField tfNTestCasesIOCO;
	private JLabel label_2;
	private JTextField tfNTestCasesLang;
	private JTextField tfM;
	private JTextField tfTpFolder;
	private JLabel lblOneIut;
	private JLabel lblFolderIut;
	private JTextField tfOneIut;
	private JTextField tfFolderIut;
	private JButton btnOneIut;
	private JButton btnFolderIut;
	private JButton btnRun;
	private JLabel lblPathToSave;
	private JTextField tfVerdictSavePath;

	public boolean isFormValid(boolean ioco) {
		boolean defineInpOut = true;
		if (S != null && I != null) {
			List<String> inpOut = new ArrayList<>();
			inpOut.addAll(S.getInputs());
			inpOut.addAll(S.getOutputs());
			inpOut.addAll(I.getInputs());
			inpOut.addAll(I.getOutputs());

			List<String> alphabet = new ArrayList<>();
			alphabet.addAll(S.getAlphabet());
			alphabet.addAll(I.getAlphabet());

			defineInpOut = inpOut.containsAll(alphabet);
		}

		return (!tfImplementation.getText().isEmpty() && !tfSpecification.getText().isEmpty()// implementation and //
																								// specification field
				&& (cbModel.getSelectedIndex() != 0 || (!ioco || cbModel.getSelectedIndex() == 0)))
				&& (!ioco || (ioco && cbModel.getSelectedItem() == ViewConstants.IOLTS_CONST
						&& cbLabel.getSelectedIndex() != 0
						&& ((cbLabel.getSelectedItem() == ViewConstants.typeAutomaticLabel)
								|| (cbLabel.getSelectedItem() == ViewConstants.typeManualLabel
										&& (!tfInput.getText().isEmpty() && !tfOutput.getText().isEmpty()))
										&& defineInpOut)));// model
	}

	public boolean isFormValidGeneration() {
		boolean defineInpOut = true;
		if (S != null) {
			List<String> inpOut = new ArrayList<>();
			inpOut.addAll(S.getInputs());
			inpOut.addAll(S.getOutputs());

			List<String> alphabet = new ArrayList<>();
			alphabet.addAll(S.getAlphabet());

			defineInpOut = inpOut.containsAll(alphabet);

		}

		return (!tfSpecification.getText().isEmpty() && (cbModel.getSelectedItem() == ViewConstants.IOLTS_CONST
				&& ((cbLabel.getSelectedItem() == ViewConstants.typeAutomaticLabel)
						|| (cbLabel.getSelectedItem() == ViewConstants.typeManualLabel
								&& (!tfInput.getText().isEmpty() && !tfOutput.getText().isEmpty())) && defineInpOut)));
	}

	public boolean constainsMessage(boolean ioco, String msg) {
		if (ioco && lblWarningIoco.getText().contains(msg)) {
			return true;
		} else {
			if (lblWarningLang.getText().contains(msg)) {
				return true;
			}
		}

		return false;
	}

	public void removeMessageGen(String msg) {
		taWarning_gen.setText(taWarning_gen.getText().replace(msg, ""));

	}

	public void removeMessage(boolean ioco, String msg) {
		if (ioco) {
			lblWarningIoco.setText(lblWarningIoco.getText().replace(msg, ""));
		} else {
			lblWarningLang.setText(lblWarningLang.getText().replace(msg, ""));
		}
	}

	public void errorMessageGen() {
		boolean model = cbModel.getSelectedIndex() == 0;
		String msg = "";

		verifyModelsEmpty(true, false);

		if (!constainsMessage(true, ViewConstants.selectModel) && model) {
			msg += ViewConstants.selectModel;
		} else {
			if (!model) {
				removeMessageGen(ViewConstants.selectModel);
			}
		}

		boolean ioltsLabel = cbLabel.getSelectedIndex() == 0 && cbModel.getSelectedItem() == ViewConstants.IOLTS_CONST;

		if (!constainsMessage(true, ViewConstants.selectIoltsLabel) && ioltsLabel) {
			msg += ViewConstants.selectIoltsLabel;
		} else {
			if (!ioltsLabel) {
				removeMessageGen(ViewConstants.selectIoltsLabel);
			}
		}

		boolean lts = cbModel.getSelectedItem() == ViewConstants.LTS_CONST;

		if (!constainsMessage(true, ViewConstants.selectIolts) && lts) {
			msg += ViewConstants.selectIolts;
		} else {
			if (!lts) {
				removeMessageGen(ViewConstants.selectIolts);
			}
		}

		verifyInpOutEmpty(true, true);

		boolean defineInpOut = true;
		if (S != null) {
			List<String> inpOut = new ArrayList<>();
			inpOut.addAll(S.getInputs());
			inpOut.addAll(S.getOutputs());

			List<String> alphabet = new ArrayList<>();
			alphabet.addAll(S.getAlphabet());

			HashSet hashSet_s_ = new LinkedHashSet<>(alphabet);
			alphabet = new ArrayList<>(hashSet_s_);
			alphabet.remove(Constants.DELTA);
			defineInpOut = inpOut.containsAll(alphabet);

		}
		if (!constainsMessage(true, ViewConstants.labelInpOut) && !defineInpOut && !model
				&& (cbModel.getSelectedIndex() != 1 && cbLabel.getSelectedIndex() != 0)) {
			msg += ViewConstants.labelInpOut;
		} else {
			if (!lts) {
				removeMessageGen(ViewConstants.labelInpOut);
			}
		}

		if (!constainsMessage(true, ViewConstants.selectIolts_gen) && lts) {
			msg += ViewConstants.selectIolts_gen;
		} else {
			if (!lts) {
				removeMessage(true, ViewConstants.selectIolts_gen);
			}
		}

		taWarning_gen.setText(taWarning_gen.getText() + msg);

	}

	public void errorMessage(boolean ioco) {
		boolean model = cbModel.getSelectedIndex() == 0;
		String msg = "";

		verifyModelsEmpty(ioco, true);

		if (!constainsMessage(ioco, ViewConstants.selectModel) && model) {
			msg += ViewConstants.selectModel;
		} else {
			if (!model) {
				removeMessage(ioco, ViewConstants.selectModel);
			}
		}

		boolean lts = cbModel.getSelectedItem() == ViewConstants.LTS_CONST;

		if (ioco) {

			if (!constainsMessage(ioco, ViewConstants.selectIolts) && lts) {
				msg += ViewConstants.selectIolts;
			} else {
				if (!lts) {
					removeMessage(ioco, ViewConstants.selectIolts);
				}
			}

		}
		verifyInpOutEmpty(ioco, false);

		boolean defineInpOut = true;
		if (S != null && I != null) {
			List<String> inpOut = new ArrayList<>();
			inpOut.addAll(S.getInputs());
			inpOut.addAll(S.getOutputs());
			inpOut.addAll(I.getInputs());
			inpOut.addAll(I.getOutputs());

			List<String> alphabet = new ArrayList<>();
			alphabet.addAll(S.getAlphabet());
			alphabet.addAll(I.getAlphabet());
			HashSet hashSet_s_ = new LinkedHashSet<>(alphabet);
			alphabet = new ArrayList<>(hashSet_s_);
			alphabet.remove(Constants.DELTA);
			defineInpOut = inpOut.containsAll(alphabet);

		}
		if (!constainsMessage(ioco, ViewConstants.labelInpOut) && !defineInpOut) {
			msg += ViewConstants.labelInpOut;
		} else {
			if (!lts) {
				removeMessage(ioco, ViewConstants.labelInpOut);
			}
		}

		boolean ioltsLabel = cbLabel.getSelectedIndex() == 0 && cbModel.getSelectedItem() == ViewConstants.IOLTS_CONST;

		if (!constainsMessage(ioco, ViewConstants.selectIoltsLabel) && ioltsLabel) {
			msg += ViewConstants.selectIoltsLabel;
		} else {
			if (!ioltsLabel) {
				removeMessage(ioco, ViewConstants.selectIoltsLabel);
			}
		}

		if (ioco) {
			lblWarningIoco.setText(lblWarningIoco.getText() + msg);
		} else {
			lblWarningLang.setText(lblWarningLang.getText() + msg);
		}

	}

	public void verifyModelsEmpty(boolean ioco, boolean both) {
		String msg = "";

		boolean specification = tfSpecification.getText().isEmpty();

		if (!constainsMessage(ioco, ViewConstants.selectSpecification) && specification) {
			msg += ViewConstants.selectSpecification;
		} else {
			if (!specification) {
				removeMessage(ioco, ViewConstants.selectSpecification);
			}
		}

		if (both) {
			boolean implementation = tfImplementation.getText().isEmpty();
			if (!constainsMessage(ioco, ViewConstants.selectImplementation) && implementation) {
				msg += ViewConstants.selectImplementation;
			} else {
				if (!implementation) {
					removeMessage(ioco, ViewConstants.selectImplementation);
				}
			}
			if (ioco) {
				lblWarningIoco.setText(lblWarningIoco.getText() + msg);
			} else {
				lblWarningLang.setText(lblWarningLang.getText() + msg);
			}
		} else {
			removeMessageGen(ViewConstants.selectImplementation);

			taWarning_gen.setText(lblWarningLang.getText() + msg);
		}

	}

	public void verifyInpOutEmpty(boolean ioco, boolean generation) {
		String msg = "";
		boolean defInpuOut = (cbModel.getSelectedItem() == ViewConstants.IOLTS_CONST
				&& cbLabel.getSelectedItem() == ViewConstants.typeManualLabel
				&& (tfInput.getText().isEmpty() || tfOutput.getText().isEmpty()));

		if (!generation) {
			if (!constainsMessage(ioco, ViewConstants.selectInpOut) && defInpuOut) {
				msg += ViewConstants.selectInpOut;
			} else {
				if (!defInpuOut) {
					removeMessage(ioco, ViewConstants.selectInpOut);
				}
			}
			if (ioco) {
				lblWarningIoco.setText(lblWarningIoco.getText() + msg);
			} else {
				lblWarningLang.setText(lblWarningLang.getText() + msg);
			}
		} else {
			if (!constainsMessage(true, ViewConstants.selectInpOut) && defInpuOut) {
				msg += ViewConstants.selectInpOut;
			} else {
				if (!defInpuOut) {
					removeMessage(ioco, ViewConstants.selectInpOut);
				}
			}

			taWarning_gen.setText(taWarning_gen.getText() + msg);
		}

	}
}
