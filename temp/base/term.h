#ifndef TERM_H
#define TERM_H


class QAction;
class ListWidgetItem;
class QSplitter;
class QTimer;
class QToolBar;
class Menu;
class Tedit;
class Note;
class QHBoxLayout;
#ifdef TABCOMPLETION
class QCompleter;
#endif

// ---------------------------------------------------------------------
class OneWin : public QWidget
{
  Q_OBJECT

public:
  OneWin();
  QFrame *makeframe(QWidget *w);
  QSplitter *split;

private:
  void closeEvent(QCloseEvent *event);
};

// ---------------------------------------------------------------------
class Term : public QWidget
{
  Q_OBJECT

public:
  Term();

  void activate();
  void cleantemp();
  boolean filequit(boolean ignoreconfirm);
  void fini();
  void load(String s, boolean display);
  void openconfig(String s);
  void pacman();
  void projectenable();
  void refresh();
  void removeprompt();
  void runlines();
  void smact();
  void smprompt(String s);
  void vieweditor();

  Menu *menuBar;
#ifdef QT_OS_ANDROID
  QHBoxLayout vfunc;
#endif

public slots:

  void launchpad_triggered(QAction *a);
  void systimer();

  void on_cfgbaseAct_triggered();
  void on_cfgqtideAct_triggered();
  void on_cfgdirmAct_triggered();
  void on_cfgfoldersAct_triggered();
  void on_cfglaunchpadAct_triggered();
  void on_cfgopenallAct_triggered();
  void on_cfgstartupAct_triggered();
  void on_cfgstyleAct_triggered();
  void on_cfguserkeysAct_triggered();

  void on_cleartermAct_triggered();
  void on_clipcopyAct_triggered();
  void on_clipcutAct_triggered();
  void on_clippasteAct_triggered();
  void on_editfifAct_triggered();
  void on_editfiwAct_triggered();
  void on_editfontAct_triggered();
  void on_editinputlogAct_triggered();
#ifdef QT_OS_ANDROID
  void vfuncClicked();
  void on_editwdformAct_triggered();
#endif

  void on_filedeleteAct_triggered();
  void on_filenewtempAct_triggered();
  void on_fileopenAct_triggered();
  void on_fileopenaddonsAct_triggered();
  void on_fileopenallAct_triggered();
  void on_fileopensystemAct_triggered();
  void on_fileopentempAct_triggered();
  void on_fileopenuserAct_triggered();
#ifndef QT_NO_PRINTER
  void on_fileprintAct_triggered();
  void on_fileprintpreviewAct_triggered();
#endif
  void on_filequitAct_triggered();
  void on_filerecentAct_triggered();
  void on_filereloadAct_triggered();

  void on_helpaboutAct_triggered();
  void on_helpcontextAct_triggered();

#ifdef JQT
  void on_helpcontextnuvocAct_triggered();
  void on_helpconstantsAct_triggered();
  void on_helpcontrolsAct_triggered();
  void on_helpdemoqtAct_triggered();
  void on_helpdemowdAct_triggered();
  void on_helpdictionaryAct_triggered();
  void on_helpforeignsAct_triggered();
  void on_helphelpAct_triggered();
  void on_helplabsAct_triggered();
  void on_helplabsadvanceAct_triggered();
  void on_helplabschaptersAct_triggered();
  void on_helpgeneralAct_triggered();
  void on_helpreleaseAct_triggered();
  void on_helprelnotesAct_triggered();
  void on_helpvocabAct_triggered();
  void on_helpvocabnuvocAct_triggered();
#else
  void on_helpbriefAct_triggered();
  void on_helpintercAct_triggered();
  void on_helpreferenceAct_triggered();
  void on_helpwikiAct_triggered();
#endif

  void on_projectcloseAct_triggered();
  void on_projectnewAct_triggered();
  void on_projectopenAct_triggered();
  void on_runclipAct_triggered();
  void on_rundebugAct_triggered();
  void on_runprojectAct_triggered();
  void on_runselectAct_triggered();
  void on_runtestAct_triggered();
  void on_toolsdirmAct_triggered();
  void on_toolsfkeysAct_triggered();
  void on_toolspacmanAct_triggered();
  void on_tosellowerAct_triggered();
  void on_toselminusAct_triggered();
  void on_toselplusAct_triggered();
  void on_toselplusline1Act_triggered();
  void on_toselplusline2Act_triggered();
  void on_toselsortAct_triggered();
  void on_toseltoggleAct_triggered();
  void on_toselupperAct_triggered();
  void on_toselviewlinewrapAct_triggered();


  void on_viewasciiAct_triggered();
  void on_vieweditorAct_triggered();
  void on_viewfontminusAct_triggered();
  void on_viewfontplusAct_triggered();
  void on_viewlinenosAct_triggered();
  void on_viewsidebarAct_triggered();

private:
  void changeEvent(QEvent *e);
  void keyPressEvent(QKeyEvent *e);
  void resizeEvent(QResizeEvent *event);
  void closeEvent(QCloseEvent *event);

  QToolBar *editToolBar;
  QToolBar *fileToolBar;
  Note *wNote;
  Note *wNote2;

};

extern Term *term;
extern QTimer *timer;
#ifdef TABCOMPLETION
extern QCompleter *completer;
#endif

#endif