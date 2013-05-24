package com.ec1513079.dogear;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Executor;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.artifex.mupdfdemo.*;

import com.ec1513079.dogear.R;
import com.ec1513079.dogear.R.layout;

public class PDFReaderFragment extends Fragment {
	
	/* PDF File */
	public static final String PDF_FILE_PATH = "pdf_file_path";
	public static final String PDF_DISPLAYED_PAGE = "pdf_displayed_page";

	private MuPDFCore    core;
	private String       mFileName;
	private String       mFilePath;
	private MuPDFReaderView mDocView;
	private View         mButtonsView;
	private boolean      mButtonsVisible;
	private EditText     mPasswordView;
	private SeekBar      mPageSlider;
	private int          mPageSliderRes;
	private TextView     mPageNumberView;
	private TextView     mInfoView;
	private AlertDialog.Builder mAlertBuilder;
	private final Handler mHandler = new Handler();
	private AsyncTask<Void,Void,MuPDFAlert> mAlertTask;
	
	public PDFReaderFragment() {
	}
	
	private MuPDFCore openFile(String path)
	{
		int lastSlashPos = path.lastIndexOf('/');
		mFilePath = path;
		mFileName = new String(lastSlashPos == -1
					? path
					: path.substring(lastSlashPos+1));
		System.out.println("Trying to open "+path);
		try
		{
			core = new MuPDFCore(this.getActivity(), path);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		return core;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mAlertBuilder = new AlertDialog.Builder(this.getActivity());

		if (core == null) {
			String filePath = getArguments().getString(PDF_FILE_PATH);
			core = openFile(filePath);
			
			if (core != null && core.needsPassword()) {
				requestPassword(savedInstanceState);
				return;
			}
			
			if (core.countPages() == 0)
				core = null;

			if (core == null)
			{
				AlertDialog alert = mAlertBuilder.create();
				alert.setTitle(R.string.cannot_open_document);
				alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dismiss),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								//TODO : 終了命令
							}
						});
				alert.show();
				return;
			}
		}
	}
	
	public void requestPassword(final Bundle savedInstanceState) {
		mPasswordView = new EditText(this.getActivity());
		mPasswordView.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
		mPasswordView.setTransformationMethod(new PasswordTransformationMethod());

		AlertDialog alert = mAlertBuilder.create();
		alert.setTitle(R.string.enter_password);
		alert.setView(mPasswordView);
		alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.okay),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (core.authenticatePassword(mPasswordView.getText().toString())) {
					//TODO: 再描画命令
				} else {
					requestPassword(savedInstanceState);
				}
			}
		});
		alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alert.show();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//TODO : 読み込み失敗時の画面表示
		if (core == null)
			return new View(this.getActivity());

		// First create the document view
		mDocView = new MuPDFReaderView(this.getActivity()) {
			@Override
			protected void onMoveToChild(int i) {
				if (core == null)
					return;
				mPageNumberView.setText(String.format("%d / %d", i + 1,
						core.countPages()));
				mPageSlider.setMax((core.countPages() - 1) * mPageSliderRes);
				mPageSlider.setProgress(i * mPageSliderRes);
				super.onMoveToChild(i);
			}

			@Override
			protected void onTapMainDocArea() {
				if (!mButtonsVisible) {
					showButtons();
				} else {
					hideButtons();
				}
			}

			@Override
			protected void onDocMotion() {
				hideButtons();
			}
		};
		mDocView.setAdapter(new MuPDFPageAdapter(this.getActivity(), core));

		// Make the buttons overlay, and store all its
		// controls in variables
		mButtonsView = getLayoutInflater(savedInstanceState)
				.inflate(com.ec1513079.dogear.R.layout.overlay_layout,null);
		mPageSlider = (SeekBar)mButtonsView.findViewById(R.id.pageSlider);
		mPageNumberView = (TextView)mButtonsView.findViewById(R.id.pageNumber);
		mInfoView = (TextView)mButtonsView.findViewById(R.id.info);
		mPageNumberView.setVisibility(View.INVISIBLE);
		mInfoView.setVisibility(View.INVISIBLE);
		mPageSlider.setVisibility(View.INVISIBLE);

		// Set up the page slider
		int smax = Math.max(core.countPages()-1,1);
		mPageSliderRes = ((10 + smax - 1)/smax) * 2;

		// Activate the seekbar
		mPageSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				mDocView.setDisplayedViewIndex((seekBar.getProgress()+mPageSliderRes/2)/mPageSliderRes);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (core == null)
					return;
				mPageNumberView.setText(String.format("%d / %d", 
						((progress+mPageSliderRes/2)/mPageSliderRes)+1, core.countPages()));
			}
		});

		if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false))
			showButtons();
		
		if (savedInstanceState !=  null && savedInstanceState.containsKey(PDF_DISPLAYED_PAGE))
			mDocView.setDisplayedViewIndex(savedInstanceState.getInt(PDF_DISPLAYED_PAGE));

		// Stick the document view and the buttons overlay into a parent view
		RelativeLayout layout = new RelativeLayout(this.getActivity());
		layout.addView(mDocView);
		layout.addView(mButtonsView);
		layout.setBackgroundResource(R.drawable.tiled_background);
		
		// Move page
		int page = getArguments().getInt(PDF_DISPLAYED_PAGE, 0);
		mDocView.setDisplayedViewIndex(page);

		return layout;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.pdfreader_layout, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_to_grid_view:
			showPageTableView();
			return false;
		case R.id.action_pdf_open_in:
			openInPDF();
			return false;
		case R.id.action_add_bookmark:
			Util.addBookmark(getActivity(), new File(mFilePath), mDocView.getDisplayedViewIndex());
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mFilePath != null && mDocView != null) 
			outState.putString(PDF_FILE_PATH, mFilePath);
		if (!mButtonsVisible)
			outState.putBoolean("ButtonsHidden", true);
		if (core != null && mDocView != null)
			outState.putInt(PDF_DISPLAYED_PAGE, mDocView.getDisplayedViewIndex());
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy()
	{
		if (core != null)
			core.onDestroy();
		if (mAlertTask != null) {
			mAlertTask.cancel(true);
			mAlertTask = null;
		}
		core = null;
		super.onDestroy();
	}

	private void showButtons() {
		if (core == null)
			return;
		if (!mButtonsVisible) {
			mButtonsVisible = true;
			// Update page number text and slider
			int index = mDocView.getDisplayedViewIndex();
			mPageNumberView.setText(String.format("%d / %d", index+1, core.countPages()));
			mPageSlider.setMax((core.countPages()-1)*mPageSliderRes);
			mPageSlider.setProgress(index*mPageSliderRes);

			Animation anim = new TranslateAnimation(0, 0, mPageSlider.getHeight(), 0);
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					mPageSlider.setVisibility(View.VISIBLE);
				}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) {
					mPageNumberView.setVisibility(View.VISIBLE);
				}
			});
			mPageSlider.startAnimation(anim);
		}
	}

	private void hideButtons() {
		if (mButtonsVisible) {
			mButtonsVisible = false;

			Animation anim = new TranslateAnimation(0, 0, 0, mPageSlider.getHeight());
			anim.setDuration(200);
			anim.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation animation) {
					mPageNumberView.setVisibility(View.INVISIBLE);
				}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) {
					mPageSlider.setVisibility(View.INVISIBLE);
				}
			});
			mPageSlider.startAnimation(anim);
		}
	}

	private void showInfo(String message) {
		mInfoView.setText(message);

		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this.getActivity(), R.animator.info);
			set.setTarget(mInfoView);
			set.addListener(new Animator.AnimatorListener() {
				public void onAnimationStart(Animator animation) {
					mInfoView.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animator animation) {
				}

				public void onAnimationEnd(Animator animation) {
					mInfoView.setVisibility(View.INVISIBLE);
				}

				public void onAnimationCancel(Animator animation) {
				}
			});
			set.start();
		} else {
			mInfoView.setVisibility(View.VISIBLE);
			mHandler.postDelayed(new Runnable() {
				public void run() {
					mInfoView.setVisibility(View.INVISIBLE);
				}
			}, 500);
		}
	}
	
	private void openInPDF() {
		try {
			File file = new File(getArguments().getString(PDF_FILE_PATH));
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/pdf");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private void showPageTableView() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.putExtra(PDFPageTableActivity.NEED_MOVE_TO_PAGE, mDocView.getDisplayedViewIndex());
		intent.putExtra(PDFPageTableActivity.PDF_FILE_PATH, getArguments().getString(PDF_FILE_PATH));
		intent.setClass(this.getActivity(), PDFPageTableActivity.class);
		startActivityForResult(intent, PDFPageTableActivity.RESULT_NEED_MOVE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case PDFPageTableActivity.RESULT_NEED_MOVE:
			if (data.hasExtra(PDFPageTableActivity.NEED_MOVE_TO_PAGE)) {
				int move_to_page = data.getIntExtra(PDFPageTableActivity.NEED_MOVE_TO_PAGE, 0);
				mDocView.setDisplayedViewIndex(move_to_page);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
