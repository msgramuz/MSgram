package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.StopVirus;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;

/**
 * MSgram's own settings. Everything here is client-side only and never leaves the device.
 */
public class MSgramSettingsActivity extends UniversalFragment {

    private static final int BUTTON_HIDE_ADS = 1;
    private static final int BUTTON_STOP_VIRUS = 2;
    private static final int BUTTON_CLEAN_NOW = 3;
    private static final int BUTTON_CHANNEL = 4;
    private static final int BUTTON_JOIN = 5;

    public static final String OFFICIAL_CHANNEL = "MSgramUz";

    private final boolean insideTabs;

    public MSgramSettingsActivity() {
        this(false);
    }

    public MSgramSettingsActivity(boolean insideTabs) {
        this.insideTabs = insideTabs;
    }

    @Override
    public View createView(Context context) {
        final View view = super.createView(context);
        if (insideTabs) {
            // this is a tab, not something you navigated into
            actionBar.setBackButtonDrawable(null);
        }
        return view;
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.MSgramSettings);
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asHeader(getString(R.string.MSgramAbout)));
        items.add(UItem.asButton(BUTTON_CHANNEL, R.drawable.msg_channel,
                getString(R.string.MSgramOfficialChannel), "@" + OFFICIAL_CHANNEL));
        items.add(UItem.asShadow(getString(R.string.MSgramChannelInfo)));

        items.add(UItem.asHeader(getString(R.string.MSgramStopVirus)));
        items.add(UItem.asCheck(BUTTON_STOP_VIRUS, getString(R.string.MSgramStopVirusEnable))
                .setChecked(SharedConfig.stopVirusEnabled));
        items.add(UItem.asShadow(getString(R.string.MSgramStopVirusInfo)));
        items.add(UItem.asButton(BUTTON_CLEAN_NOW, getString(R.string.MSgramStopVirusCleanNow)));
        items.add(UItem.asShadow(getString(R.string.MSgramStopVirusCleanNowInfo)));

        items.add(UItem.asHeader(getString(R.string.MSgramAds)));
        items.add(UItem.asCheck(BUTTON_HIDE_ADS, getString(R.string.MSgramHideAds))
                .setChecked(SharedConfig.hideSponsoredMessages));
        items.add(UItem.asShadow(getString(R.string.MSgramHideAdsInfo)));

        items.add(UItem.asButton(BUTTON_JOIN, R.drawable.msg_channel, getString(R.string.MSgramJoinChannel)));
        items.add(UItem.asShadow(LocaleController.formatString(R.string.MSgramDisclaimer,
                BuildVars.BUILD_VERSION_STRING)));

        if (insideTabs) {
            // keep the last row clear of the floating tab bar
            items.add(UItem.asSpace(dp(DialogsActivity.MAIN_TABS_HEIGHT_WITH_MARGINS)));
        }
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == BUTTON_CHANNEL || item.id == BUTTON_JOIN) {
            getMessagesController().openByUserName(OFFICIAL_CHANNEL, this, 1);
        } else if (item.id == BUTTON_STOP_VIRUS) {
            SharedConfig.toggleStopVirus();
            ((TextCheckCell) view).setChecked(SharedConfig.stopVirusEnabled);
        } else if (item.id == BUTTON_HIDE_ADS) {
            SharedConfig.toggleHideSponsoredMessages();
            ((TextCheckCell) view).setChecked(SharedConfig.hideSponsoredMessages);
        } else if (item.id == BUTTON_CLEAN_NOW) {
            new Thread(() -> {
                final int removed = StopVirus.cleanDownloads();
                AndroidUtilities.runOnUIThread(() -> {
                    if (getContext() == null) {
                        return;
                    }
                    BulletinFactory.of(this).createSimpleBulletin(
                            R.raw.ic_delete,
                            removed > 0
                                    ? LocaleController.formatPluralString("MSgramStopVirusCleaned", removed)
                                    : getString(R.string.MSgramStopVirusNothing)
                    ).show();
                });
            }, "MSgramStopVirusClean").start();
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
