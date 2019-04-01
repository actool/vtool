package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import algorithm.*;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import model.*;
import parser.ImportAutFile;

import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ConformanceView extends JFrame {

	private JPanel contentPane;
	private JTextField tfImplementacao;
	private JTextField tfEspecificacao;
	private JTextField tfLinguagemD;
	private JTextField tfLinguagemF;
	private JTextField tfResultado;
	private JButton btnDetalhe;

	private String pathImplementação2;
	private String pathEspecificacao2;
	private String caminhosFalha;
	JFileChooser fc = new JFileChooser();
	private JTextField tfEntrada;
	private JTextField tfSaida;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private String tipoRotuloDefinido = "?in, !out";
	private String tipoRotuloManual = "define input and output manually";

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
		FileFilter autFilter = new FileTypeFilter(".aut", "Arquivos Aut");
		fc.addChoosableFileFilter(autFilter);
		fc.setAcceptAllFileFilterUsed(false);
	}

	public void getImplementacaoLingPath() {
		caminhosFalha = "";
		tfResultado.setText("");
		btnDetalhe.setVisible(false);
		try {
			configFilterFile();
			fc.showOpenDialog(ConformanceView.this);
			tfImplementacao.setText(fc.getSelectedFile().getName());
			pathImplementação2 = fc.getSelectedFile().getAbsolutePath();
			fc.setCurrentDirectory(fc.getSelectedFile().getParentFile());
		} catch (Exception e) {

		}
	}

	public void getEspecificacaoLingPath() {
		caminhosFalha = "";
		tfResultado.setText("");
		btnDetalhe.setVisible(false);
		try {
			configFilterFile();
			fc.showOpenDialog(ConformanceView.this);
			tfEspecificacao.setText(fc.getSelectedFile().getName());
			pathEspecificacao2 = fc.getSelectedFile().getAbsolutePath();
		} catch (Exception e) {
		}

	}

	public boolean expressaoRegularValida(String exp) {
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
		setTitle("VTool");
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

		JLabel lblNewLabel_1 = new JLabel("Implementation");
		lblNewLabel_1.setBackground(SystemColor.windowBorder);
		lblNewLabel_1.setForeground(SystemColor.controlHighlight);
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 13));
		lblNewLabel_1.setBounds(20, 190, 157, 22);
		panel_2.add(lblNewLabel_1);

		JLabel lblEspecificao = new JLabel("Model");
		lblEspecificao.setForeground(SystemColor.controlHighlight);
		lblEspecificao.setFont(new Font("Dialog", Font.BOLD, 13));
		lblEspecificao.setBounds(21, 247, 99, 14);
		panel_2.add(lblEspecificao);

		tfImplementacao = new JTextField();
		tfImplementacao.setForeground(SystemColor.window);
		tfImplementacao.setBackground(SystemColor.windowBorder);//SystemColor.control
		tfImplementacao.setToolTipText("aceita somente arquivos .aut");
		tfImplementacao.setFont(new Font("Dialog", Font.BOLD, 13));
		tfImplementacao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				getImplementacaoLingPath();
			}
		});
		tfImplementacao.setColumns(10);
		tfImplementacao.setBounds(20, 211, 440, 26);			
		tfImplementacao.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfImplementacao);

		tfEspecificacao = new JTextField();
		tfEspecificacao.setForeground(SystemColor.control);
		tfEspecificacao.setBackground(SystemColor.windowBorder);
		tfEspecificacao.setToolTipText("aceita somente arquivos .aut");
		tfEspecificacao.setFont(new Font("Dialog", Font.BOLD, 13));
		tfEspecificacao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getEspecificacaoLingPath();
			}
		});
		tfEspecificacao.setColumns(10);
		tfEspecificacao.setBounds(20, 261, 443, 26);
		tfEspecificacao.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfEspecificacao);

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

		tfLinguagemD = new JTextField();
		tfLinguagemD.setForeground(SystemColor.control);
		tfLinguagemD.setBackground(SystemColor.windowBorder);
		tfLinguagemD.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				caminhosFalha = "";
				tfResultado.setText("");
				btnDetalhe.setVisible(false);
			}
		});
		tfLinguagemD.setFont(new Font("Dialog", Font.BOLD, 13));
		tfLinguagemD.setBounds(21, 315, 487, 26);
		tfLinguagemD.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfLinguagemD);
		tfLinguagemD.setColumns(10);

		tfLinguagemF = new JTextField();
		tfLinguagemF.setForeground(SystemColor.control);
		tfLinguagemF.setBackground(SystemColor.windowBorder);
		tfLinguagemF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				caminhosFalha = "";
				tfResultado.setText("");
				btnDetalhe.setVisible(false);
			}
		});
		tfLinguagemF.setFont(new Font("Dialog", Font.BOLD, 13));
		tfLinguagemF.setBounds(21, 378, 487, 26);
		tfLinguagemF.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfLinguagemF);
		tfLinguagemF.setColumns(10);

		tfResultado = new JTextField();
		tfResultado.setFont(new Font("Dialog", Font.BOLD, 13));
		tfResultado.setEnabled(false);
		tfResultado.setBounds(20, 494, 285, 36);
		panel_2.add(tfResultado);
		tfResultado.setColumns(10);

		JButton btnVerificarConformidadeLinguagem = new JButton("Verify conformance");
		btnVerificarConformidadeLinguagem.setBackground(new Color(192, 192, 192));
		btnVerificarConformidadeLinguagem.setFont(new Font("Dialog", Font.BOLD, 13));

		btnVerificarConformidadeLinguagem.setBounds(315, 430, 193, 45);
		panel_2.add(btnVerificarConformidadeLinguagem);

		JLabel lblResult = new JLabel("Veredict");
		lblResult.setForeground(SystemColor.controlHighlight);
		lblResult.setFont(new Font("Dialog", Font.BOLD, 13));
		lblResult.setBounds(20, 468, 102, 26);
		panel_2.add(lblResult);

		JLabel lblexpressoRegular = new JLabel("(Regex: +,*,())");
		lblexpressoRegular.setForeground(SystemColor.scrollbar);
		lblexpressoRegular.setFont(new Font("Dialog", Font.BOLD, 12));
		lblexpressoRegular.setBounds(432, 333, 79, 36);
		panel_2.add(lblexpressoRegular);

		JLabel lblexpressoRegular_1 = new JLabel("(Regex: +,*,())");
		lblexpressoRegular_1.setForeground(SystemColor.scrollbar);
		lblexpressoRegular_1.setFont(new Font("Dialog", Font.BOLD, 12));
		lblexpressoRegular_1.setBounds(432, 393, 93, 36);
		panel_2.add(lblexpressoRegular_1);

		btnDetalhe = new JButton("Show test cases");
		btnDetalhe.setBackground(SystemColor.activeCaptionBorder);
		btnDetalhe.setVisible(false);
		btnDetalhe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFrame frame = new JFrame();
				frame.setVisible(true);
				frame.setSize(500, 500);
				JPanel panel = new JPanel();
				TextArea ta = new TextArea(25, 60);
				//caminhosFalha = (caminhosFalha.equals("")?"Test suite is empty, no fault found!":caminhosFalha);
				
				ta.setText(caminhosFalha);
				JScrollPane scrolltxt = new JScrollPane(ta);
				scrolltxt.setBounds(3, 3, 400, 400);

				panel.add(scrolltxt);
				frame.getContentPane().add(panel);
			}
		});
		btnDetalhe.setFont(new Font("Dialog", Font.BOLD, 13));
		btnDetalhe.setBounds(315, 494, 193, 36);
		panel_2.add(btnDetalhe);

		JButton button = new JButton("");
		button.setBackground(SystemColor.activeCaptionBorder);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getImplementacaoLingPath();
			}
		});
		button.setIcon(
				new ImageIcon("C:\\Users\\camil\\Google Drive\\UEL\\svn\\mbt\\camila_mestrado\\img\\folder.png"));
		button.setBounds(462, 211, 39, 28);
		panel_2.add(button);

		JButton button_1 = new JButton("");
		button_1.setBackground(SystemColor.activeCaptionBorder);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getEspecificacaoLingPath();
			}
		});
		button_1.setIcon(
				new ImageIcon("C:\\Users\\camil\\Google Drive\\UEL\\svn\\mbt\\camila_mestrado\\img\\folder.png"));
		button_1.setBounds(462, 259, 39, 28);
		panel_2.add(button_1);

		JLabel lblSaida = new JLabel("Output");
		lblSaida.setForeground(SystemColor.controlHighlight);
		lblSaida.setFont(new Font("Dialog", Font.BOLD, 13));
		lblSaida.setBounds(265, 121, 48, 22);
		lblSaida.setVisible(false);
		panel_2.add(lblSaida);

		JLabel lblModelo = new JLabel("Kind of models");
		lblModelo.setForeground(SystemColor.controlHighlight);
		lblModelo.setFont(new Font("Dialog", Font.BOLD, 13));
		lblModelo.setBounds(21, 64, 104, 22);
		panel_2.add(lblModelo);

		JLabel lblEntrada = new JLabel("Input");
		lblEntrada.setForeground(SystemColor.controlHighlight);
		lblEntrada.setFont(new Font("Dialog", Font.BOLD, 13));
		lblEntrada.setBounds(21, 121, 54, 22);
		lblEntrada.setVisible(false);
		panel_2.add(lblEntrada);

		tfEntrada = new JTextField();
		tfEntrada.setForeground(SystemColor.control);
		tfEntrada.setBackground(SystemColor.windowBorder);
		tfEntrada.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				caminhosFalha = "";
				tfResultado.setText("");
				btnDetalhe.setVisible(false);
			}
		});
		tfEntrada.setToolTipText("");
		tfEntrada.setFont(new Font("Dialog", Font.BOLD, 13));
		tfEntrada.setColumns(10);
		tfEntrada.setBounds(21, 141, 234, 22);
		tfEntrada.setVisible(false);

		tfEntrada.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfEntrada);

		tfSaida = new JTextField();
		tfSaida.setForeground(SystemColor.control);
		tfSaida.setBackground(SystemColor.windowBorder);
		tfSaida.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				caminhosFalha = "";
				tfResultado.setText("");
				btnDetalhe.setVisible(false);
			}
		});
		tfSaida.setFont(new Font("Dialog", Font.BOLD, 13));
		tfSaida.setColumns(10);
		tfSaida.setBounds(265, 141, 236, 22);
		tfSaida.setVisible(false);
		tfSaida.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(tfSaida);

		JLabel lblLegendaEnt = new JLabel("(label split by comma)");
		lblLegendaEnt.setBackground(SystemColor.windowBorder);
		lblLegendaEnt.setForeground(SystemColor.scrollbar);
		lblLegendaEnt.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLegendaEnt.setBounds(126, 165, 129, 14);
		lblLegendaEnt.setVisible(false);
		panel_2.add(lblLegendaEnt);

		JLabel lblLegendaSaida = new JLabel("(label split by comma)");
		lblLegendaSaida.setBackground(SystemColor.windowBorder);
		lblLegendaSaida.setForeground(SystemColor.scrollbar);
		lblLegendaSaida.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLegendaSaida.setBounds(380, 165, 164, 14);
		lblLegendaSaida.setVisible(false);
		panel_2.add(lblLegendaSaida);

		JList list = new JList();
		list.setBounds(371, 25, 1, 1);
		panel_2.add(list);

		JLabel lblTipoConformidade = new JLabel("Conformance");
		lblTipoConformidade.setForeground(SystemColor.controlHighlight);
		lblTipoConformidade.setToolTipText("");
		lblTipoConformidade.setFont(new Font("Microsoft JhengHei Light", Font.BOLD, 13));
		lblTipoConformidade.setBounds(21, 18, 129, 22);
		panel_2.add(lblTipoConformidade);

		JLabel lblRotulo = new JLabel("Label");
		lblRotulo.setForeground(SystemColor.controlHighlight);
		lblRotulo.setFont(new Font("Dialog", Font.BOLD, 13));
		lblRotulo.setBounds(265, 64, 60, 22);
		lblRotulo.setVisible(false);
		panel_2.add(lblRotulo);

		JComboBox cbRotulo = new JComboBox();
		cbRotulo.setForeground(SystemColor.control);
		cbRotulo.setBackground(SystemColor.windowBorder);
		cbRotulo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getItem().equals(tipoRotuloManual)) {
					lblEntrada.setVisible(true);
					lblSaida.setVisible(true);
					tfEntrada.setVisible(true);
					lblLegendaEnt.setVisible(true);
					lblLegendaSaida.setVisible(true);
					tfSaida.setVisible(true);
				} else {
					lblEntrada.setVisible(false);
					lblSaida.setVisible(false);
					tfEntrada.setVisible(false);
					tfSaida.setVisible(false);
					lblLegendaEnt.setVisible(false);
					lblLegendaSaida.setVisible(false);
				}

				tfEntrada.setText("");
				tfSaida.setText("");
				
				caminhosFalha = "";
				tfResultado.setText("");
				btnDetalhe.setVisible(false);
			}
		});
		cbRotulo.setModel(
				new DefaultComboBoxModel(new String[] { "", this.tipoRotuloDefinido, this.tipoRotuloManual }));
		cbRotulo.setFont(new Font("Dialog", Font.BOLD, 13));
		cbRotulo.setBounds(265, 84, 237, 26);
		cbRotulo.setVisible(false);
		cbRotulo.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(cbRotulo);

		JComboBox cbModelo = new JComboBox();
		cbModelo.setForeground(SystemColor.control);
		cbModelo.setBackground(SystemColor.windowBorder);
		cbModelo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getItem().equals("IOLTS")) {
					cbRotulo.setVisible(true);
					lblRotulo.setVisible(true);
					/*
					 * lblEntrada.setVisible(true); lblSaida.setVisible(true);
					 * tfEntrada.setVisible(true); tfSaida.setVisible(true);
					 */
				} else {
					/*
					 * lblEntrada.setVisible(false); lblSaida.setVisible(false);
					 * tfEntrada.setVisible(false); tfSaida.setVisible(false);
					 */
					cbRotulo.setVisible(false);
					lblRotulo.setVisible(false);
					tfEntrada.setVisible(false);
					tfSaida.setVisible(false);
				}

				caminhosFalha = "";
				tfResultado.setText("");
				btnDetalhe.setVisible(false);
			}
		});

		cbModelo.setModel(new DefaultComboBoxModel(new String[] { "", "IOLTS", "LTS" }));
		cbModelo.setFont(new Font("Dialog", Font.BOLD, 13));
		cbModelo.setBounds(21, 85, 234, 26);
		cbModelo.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.control));
		panel_2.add(cbModelo);

		JRadioButton rbIoco = new JRadioButton("IOCO");
		rbIoco.setForeground(SystemColor.controlHighlight);
		rbIoco.setBackground(SystemColor.windowBorder);
		rbIoco.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				cbModelo.setEnabled(false);
				cbModelo.setSelectedIndex(1);
				lblRotulo.setVisible(true);
				cbRotulo.setVisible(true);
				cbRotulo.setSelectedIndex(0);
				tfLinguagemD.enable(false);
				tfLinguagemD.setText("");
				tfLinguagemF.enable(false);
				tfLinguagemF.setText("");

				caminhosFalha = "";
				tfResultado.setText("");
				btnDetalhe.setVisible(false);
			}
		});

		buttonGroup.add(rbIoco);
		rbIoco.setFont(new Font("Dialog", Font.BOLD, 13));
		rbIoco.setBounds(142, 18, 85, 23);
		panel_2.add(rbIoco);

		JRadioButton rbConfLing = new JRadioButton("Based on language");
		rbConfLing.setForeground(SystemColor.controlHighlight);
		rbConfLing.setBackground(SystemColor.windowBorder);
		rbConfLing.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				cbModelo.setEnabled(true);
				lblRotulo.setVisible(false);
				cbRotulo.setVisible(false);
				cbModelo.setSelectedIndex(0);
				tfLinguagemD.enable(true);
				tfLinguagemD.setText("");
				tfLinguagemF.enable(true);
				tfLinguagemF.setText("");
				tfEntrada.setVisible(false);
				tfSaida.setVisible(false);
				lblEntrada.setVisible(false);
				lblSaida.setVisible(false);
				lblLegendaEnt.setVisible(false);
				lblLegendaSaida.setVisible(false);

				caminhosFalha = "";
				tfResultado.setText("");
				btnDetalhe.setVisible(false);
				
				cbRotulo.setSelectedIndex(0);
				tfEntrada.setText("");
				tfSaida.setText("");
			}
		});
		buttonGroup.add(rbConfLing);
		rbConfLing.setFont(new Font("Dialog", Font.BOLD, 13));
		rbConfLing.setBounds(229, 18, 237, 23);
		panel_2.add(rbConfLing);

		btnVerificarConformidadeLinguagem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (!tfImplementacao.getText().isEmpty() && !tfEspecificacao.getText().isEmpty()
						&& (rbConfLing.isSelected() || rbIoco.isSelected()) && cbModelo.getSelectedIndex() != 0
						&& ((cbRotulo.getSelectedIndex() != 0 && cbModelo.getSelectedIndex() == 1)
								|| (cbModelo.getSelectedIndex() == 1 && cbRotulo.getSelectedIndex() == 1
										&& !tfEntrada.getText().isEmpty() && !tfSaida.getText().isEmpty())
								|| (cbModelo.getSelectedIndex() == 2))) {
					Automaton_ conformidade = null;
					IOLTS S, I = null;
					LTS S_, I_ = null;
					boolean semLinguagem = false;

					if (rbIoco.isSelected()) {// IOCO

						try {
							if (cbRotulo.getSelectedIndex() == 2) {// entrada saída manual
								S = ImportAutFile.autToIOLTS(pathEspecificacao2, true,
										new ArrayList<String>(Arrays.asList(tfEntrada.getText().split(","))),
										new ArrayList<String>(Arrays.asList(tfSaida.getText().split(","))));

								I = ImportAutFile.autToIOLTS(pathImplementação2, true,
										new ArrayList<String>(Arrays.asList(tfEntrada.getText().split(","))),
										new ArrayList<String>(Arrays.asList(tfSaida.getText().split(","))));
							} else {
								S = ImportAutFile.autToIOLTS(pathEspecificacao2, false, new ArrayList<String>(),
										new ArrayList<String>());

								I = ImportAutFile.autToIOLTS(pathImplementação2, false, new ArrayList<String>(),
										new ArrayList<String>());
							}

							conformidade = IocoConformance.verifyIOCOConformance(S, I);
							caminhosFalha = Operations.path(S, I, conformidade);
						} catch (Exception e_) {
							JOptionPane.showMessageDialog(panel, e_.getMessage(), "Warning",
									JOptionPane.WARNING_MESSAGE);
							return;
						}

					} else {// Conf Linguagem
						try {
							if (cbModelo.getSelectedIndex() == 1) { // IOLTS

								if (cbRotulo.getSelectedIndex() == 2) {// entrada saída manual
									S = ImportAutFile.autToIOLTS(pathEspecificacao2, true,
											new ArrayList<String>(Arrays.asList(tfEntrada.getText().split(","))),
											new ArrayList<String>(Arrays.asList(tfSaida.getText().split(","))));

									I = ImportAutFile.autToIOLTS(pathImplementação2, true,
											new ArrayList<String>(Arrays.asList(tfEntrada.getText().split(","))),
											new ArrayList<String>(Arrays.asList(tfSaida.getText().split(","))));
								} else {
									S = ImportAutFile.autToIOLTS(pathEspecificacao2, false, new ArrayList<String>(),
											new ArrayList<String>());

									I = ImportAutFile.autToIOLTS(pathImplementação2, false, new ArrayList<String>(),
											new ArrayList<String>());
								}

								S_ = S.toLTS();
								I_ = I.toLTS();
							} else {
								S_ = ImportAutFile.autToLTS(pathEspecificacao2);
								I_ = ImportAutFile.autToLTS(pathImplementação2);
							}

							
							String D = "";
							D = tfLinguagemD.getText();
							if (tfLinguagemD.getText().isEmpty() && tfLinguagemF.getText().isEmpty()) {
								D = "(";
								for (String l : S_.getAlphabet()) {
									D += l + "+";
								}
								D = D.substring(0, D.length() - 1);
								D += ")*";
								//tfLinguagemD.setText(D);
								semLinguagem = true;
							}
							
							

							String F = tfLinguagemF.getText();
							
							if(expressaoRegularValida(D) && expressaoRegularValida(F)) {
								conformidade = LanguageBasedConformance.verifyLanguageConformance(S_, I_, D, F);
								caminhosFalha = Operations.path(S_, I_, conformidade);
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

					tfResultado.setText(Operations.veredict(conformidade));
					// automatoResultado2 = conformidade.toString();
					if(!caminhosFalha.equals("")) {
						btnDetalhe.setVisible(true);
					}

					if (semLinguagem) {
						JOptionPane.showMessageDialog(panel,
								"Test suite is empty, because the languages D and F were not informed!", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					// boolean linguagemD = tfLinguagemD.getText().isEmpty();
					// boolean linguagemF = tfLinguagemF.getText().isEmpty();
					boolean implementacao = tfImplementacao.getText().isEmpty();
					boolean especificacao = tfEspecificacao.getText().isEmpty();
					boolean tipoConf = (!rbConfLing.isSelected() && !rbIoco.isSelected());
					boolean rotuloIOLTS = cbRotulo.getSelectedIndex() == 0 && cbModelo.getSelectedIndex() == 1;
					boolean defEntradaSaida = (cbRotulo.getSelectedIndex() == 1 && tfEntrada.getText().isEmpty()
							&& tfSaida.getText().isEmpty());
					boolean modelo = cbModelo.getSelectedIndex() == 0;

					String msg = "";

					msg += implementacao ? "The field Implementation is required \n" : "";
					msg += especificacao ? "The field Model is required \n" : "";
					// msg += linguagemD && linguagemF ? "O campo Linguagem D ou Linguagem F deve
					// ser preenchido \n" : "";
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
