package com.triangleleft.flashcards.ui;

import com.intel.moe.natj.general.Pointer;
import com.intel.moe.natj.general.ann.Owned;
import com.intel.moe.natj.general.ann.RegisterOnStartup;
import com.intel.moe.natj.objc.ObjCRuntime;
import com.intel.moe.natj.objc.ann.ObjCClassName;
import com.intel.moe.natj.objc.ann.Property;
import com.intel.moe.natj.objc.ann.Selector;

import ios.NSObject;
import ios.uikit.UIButton;
import ios.uikit.UILabel;
import ios.uikit.UIViewController;

@com.intel.moe.natj.general.ann.Runtime(ObjCRuntime.class)
@ObjCClassName("AppViewController")
@RegisterOnStartup
public class AppViewController extends UIViewController {

    @Owned
    @Selector("alloc")
    public static native AppViewController alloc();

    @Selector("init")
    public native AppViewController init();

    protected AppViewController(Pointer peer) {
        super(peer);
    }

    public UILabel statusText = null;
    public UIButton helloButton = null;

    @Override
    public void viewDidLoad() {
        statusText = getLabel();
        helloButton = getHelloButton();
    }

    @Selector("statusText")
    @Property
    public native UILabel getLabel();

    @Selector("helloButton")
    @Property
    public native UIButton getHelloButton();

    @Selector("BtnPressedCancel_helloButton:")
    public void BtnPressedCancel_button(NSObject sender){
        statusText.setText("Hello Intel Multi-OS Engine!");
    }
}
