package gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.dom4j.DocumentException;

import core.Document;
import core.Parameters;

/**
 * The top-level class that contains the
 * main line.
 * 
 * @author Stelios
 *
 */
@SuppressWarnings("serial")
public class App extends JFrame {

	/****************************************
	 * 
	 * Private Instance Variables
	 * 
	 ****************************************/
	
	/**
	 * Reference to the App class.
	 */
	private JFrame app;
	
	/**
	 * Remember the last directory chosen for
	 * JFileChooser.
	 */
	private File lastDirectory;
	
	/**
	 * The main panel of the application
	 */
	private MainPanel mainPanel;

	/****************************************
	 * 
	 * Constructor(s)
	 * 
	 ****************************************/

	/**
	 * Constructor.
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public App() throws DocumentException, IOException {
		
		/*
		 *  Setup the JFrame
		 */
		super("CamScan");
		this.setLayout(new BorderLayout());
		this.app = this;
		Parameters.setApp(this.app);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new CamscanWindowListener());
	
		
		// Setup the menu bar
		JMenuBar menuBar = new JMenuBar();

		/*
		 * The File Menu
		 */
		
		// Setup the file menu
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		// Setup the menu items for the file menu
		JMenuItem settingsMenuItem = new JMenuItem("Settings");
		settingsMenuItem.addActionListener(new SettingsListener());
		fileMenu.add(settingsMenuItem);
		
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new QuitListener());
		fileMenu.add(quitMenuItem);
		
		/*
		 * The Import Menu
		 */
		
		// Setup the import menu
		JMenu importMenu = new JMenu("Import");
		menuBar.add(importMenu);
		
		// Setup the menu items for the import menu
		JMenuItem fromFileMenuItem = new JMenuItem("From File or Folder");
		fromFileMenuItem.addActionListener(new ImportListener());
		importMenu.add(fromFileMenuItem);
		
		
		/*
		 * The Export Menu
		 */
		
		// Setup the export menu
		JMenu exportMenu = new JMenu("Export");
		menuBar.add(exportMenu);
		
		// Setup the menu items for the export menu
		JMenuItem pdfMenuItem = new JMenuItem("PDF");
		pdfMenuItem.addActionListener(new ExportPDFListener());
		exportMenu.add(pdfMenuItem);
		
		JMenuItem imagesMenuItem = new JMenuItem("Images");
		imagesMenuItem.addActionListener(new ExportImagesListener());
		exportMenu.add(imagesMenuItem);
		
		JMenuItem textMenuItem = new JMenuItem("Extracted Text");
		textMenuItem.addActionListener(new ExportTextListener());
		exportMenu.add(textMenuItem);

		/*
		 * The About Menu
		 */
		
		// Setup the about menu
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		// Setup the menu items for the file menu
		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(new AboutListener());
		helpMenu.add(aboutMenuItem);

		/*
		 * Setup the JFrame
		 */
		
		// Assign the menu bar to this JFrame
		this.setJMenuBar(menuBar);

		// Instantiate the main panel
		this.mainPanel = new MainPanel();
		
		// CamScan logo: won't work on all platforms
		Image i = Toolkit.getDefaultToolkit().getImage(Parameters.LOGO);
		this.setIconImage(i);
		
		// Add the panel to the frame
		this.add(this.mainPanel, BorderLayout.CENTER);
		this.pack();
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}

	/****************************************
	 * 
	 * Mainline
	 * 
	 ****************************************/

	/**
	 * The main-line.
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new App();
	}

	/****************************************
	 * 
	 * Private Classes
	 * 
	 ****************************************/

	/**
	 * The ActionListener class for the quit menu item.
	 */
	private class QuitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Parameters.getCoreManager().shutdown();
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
			System.exit(0); // Exit the program
		}
	}
	
	/**
	 * The action listener which makes X-ing out the frame
	 * identical to using File->Quit.
	 */
	private class CamscanWindowListener implements WindowListener {

		public void windowClosing(WindowEvent e) {
			try {
				Parameters.getCoreManager().shutdown();
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
			System.exit(0); // Exit the program
		}
		
		public void windowOpened(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}
		
	}
	
	/**
	 * The ActionListener class for opening the settings
	 * dialog box.
	 */
	private class SettingsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unused")
			SettingsDialog sd = new SettingsDialog(Parameters.getFrame());
		}
	}
	
	/**
	 * The ActionListener class for the import from folder
	 * menu item.
	 */
	private class ImportListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {

			File file = getUserSelectionForImport();
			
			// if cancelled, then break out of this method
			if (file == null) return;
			
			try {
				if (file.isDirectory()) {
					System.out.println(file.getAbsolutePath());
					Parameters.getCoreManager().createDocumentFromFolder(file);
				} else if (file.isFile()) {
					Parameters.getCoreManager().createDocumentFromFile(file);
				} else {
					throw new IOException("Unrecognized object selected");
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(app, e.getMessage(), "Import Error", JOptionPane.ERROR_MESSAGE);
			}
			
			Parameters.getDocExpPanel().update();
		}
	}
	
	/**
	 * The ActionListener class for the export as PDF
	 * menu item.
	 */
	private class ExportPDFListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			
			File folder = getUserFileForExport(Parameters.pdfExtensions);
			
			// if cancelled, then break out of this method
			if (folder == null) return;
			
			Document workingDocument = Parameters.getCoreManager().workingDocument();
			if (workingDocument == null) return;
			
			String workingPath = workingDocument.pathname();
			
			// build the path for the output file
			String outpath = folder.getPath();
			String lowerpath = outpath.toLowerCase();
			if (!lowerpath.endsWith(".pdf")) outpath += ".pdf";
			
			try {
				Parameters.getCoreManager().exportToPdf(workingPath, outpath);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(app, e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	

	/**
	 * The ActionListener class for the export as images
	 * menu item.
	 */
	private class ExportImagesListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {

			File folder = getUserDirectoryForExport();
			
			// if cancelled, then break out of this method
			if (folder == null) return;
			
			Document workingDocument = Parameters.getCoreManager().workingDocument();
			if (workingDocument == null) return;
			
			// get the path for the output file
			String outpath = folder.getPath();
			
			try {
				Parameters.getCoreManager().exportImages(workingDocument, outpath);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(app, e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * The ActionListener class for the export as text
	 * menu item.
	 */
	private class ExportTextListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			File file = getUserFileForExport(Parameters.txtExtensions);
			
			Document workingDocument = Parameters.getCoreManager().workingDocument();
			if (workingDocument == null) return;
			
			String outpath = file.getPath();
			String lowerpath = outpath.toLowerCase();
			if (!lowerpath.endsWith(".txt") && !lowerpath.endsWith(".text") )
				outpath += ".txt";
			
			try {
				Parameters.getCoreManager().exportText(workingDocument, outpath);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(app, e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	
	private File getUserDirectoryForExport() {

		JFileChooser fc = new JFileChooser();
		int status = fc.showSaveDialog(app);
		
		if (this.lastDirectory != null) {
			fc.setCurrentDirectory(this.lastDirectory);
		}
		
		if (status == JFileChooser.APPROVE_OPTION) {
			File selection = fc.getSelectedFile();
			
			// remember the last directory from
			// which the user was selecting documents
			this.lastDirectory = selection.getParentFile();
			
			return fc.getSelectedFile();
		}
		
		else if (status == JFileChooser.CANCEL_OPTION) {
			return null;
		} else if (status == JFileChooser.ERROR_OPTION) {
			return null;
		}
		return null;
	}
	
	/**
	 * This method defines the interface by which the user selects
	 * directories for import and export.
	 * @see getUserSelection()
	 * 
	 * @return a File representing the directory selected by the user
	 */
	private File getUserFileForExport(String[] extensions) {

		FileFilter ff = new ExtensionFileFilter(extensions);
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(ff);
		int status = fc.showSaveDialog(app);
		
		if (this.lastDirectory != null) {
			fc.setCurrentDirectory(this.lastDirectory);
		}
		
		if (status == JFileChooser.APPROVE_OPTION) {
			File selection = fc.getSelectedFile();
			
			// remember the last directory from
			// which the user was selecting documents
			this.lastDirectory = selection.getParentFile();
			
			return fc.getSelectedFile();
		}
		
		else if (status == JFileChooser.CANCEL_OPTION) {
			return null;
		} else if (status == JFileChooser.ERROR_OPTION) {
			return null;
		}
		return null;
	}
	
	/**
	 * This method defines the interface by which the user selects
	 * files OR directories for import and export.
	 * @see getUserDirectory()
	 * 
	 * @return a File representing the directory selected by the user
	 */
	private File getUserSelectionForImport() {

		FileFilter ff = new ExtensionFileFilter(Parameters.imgExtensions);
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(ff);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		if (this.lastDirectory != null) {
			fc.setCurrentDirectory(this.lastDirectory);
		}
		
		int status = fc.showOpenDialog(app);
		
		// if a valid file for import is selected
		if (status == JFileChooser.APPROVE_OPTION) {
			File selection = fc.getSelectedFile();
			
			// remember the last directory from
			// which the user was selecting documents
			this.lastDirectory = selection.getParentFile();
			
			return selection;
		}
		
		else if (status == JFileChooser.CANCEL_OPTION) {
			return null;
		} else if (status == JFileChooser.ERROR_OPTION) {
			return null;
		}
		return null;
	}
	
	/**
	 * The ActionListener class for the about menu item.
	 */
	private class AboutListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(app,
					"<html> CamScan, version 0.1 <br><br> " +
					"Copyright 2011. All rights reserved. <br><br>" +
					"The program is provided AS IS with NO WARRANTY OF ANY KIND, <br>" +
					"INCLUDING THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS <br>" +
					"FOR A PARTICULAR PURPOSE.</html>",
					"About CamScan",
					JOptionPane.PLAIN_MESSAGE);
		}	
	}
}
