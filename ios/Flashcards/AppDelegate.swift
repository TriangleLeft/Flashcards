//
//  AppDelegate.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 15.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import CoreData
import AFNetworkActivityLogger
import IQKeyboardManagerSwift
import DrawerController

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    var accountModule: AccountModule?
    var settingsModule: SettingsModule?
    var loginModule: LoginModule?
    var vocabularyModule: VocabularyModule?
    var flashcardsModule: FlashcardsModule?
    var mainThreadExecutor: MainThreadExecutor?
    
    
    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        window = UIWindow(frame: UIScreen.mainScreen().bounds)
        IQKeyboardManager.sharedManager().enable = true
        IQKeyboardManager.sharedManager().enableAutoToolbar = false
        
        let consoleLogger = AFNetworkActivityLogger.sharedLogger().loggers.first as! AFNetworkActivityConsoleLogger
        consoleLogger.level = AFHTTPRequestLoggerLevel.AFLoggerLevelInfo
        AFNetworkActivityLogger.sharedLogger().startLogging()
        
        let gson = ComGoogleGsonGsonBuilder().registerTypeAdapterFactoryWithComGoogleGsonTypeAdapterFactory(AutoValueAdapterFactory()).create()
        
        let storage = IOSPersistentStorage(gson)
        let wordRepository = IOSVocabularyWordsRepository()
        
        let restService = ObjRestService(gson: gson)
        
        mainThreadExecutor = MainThreadExecutor()
        accountModule = SimpleAccountModule(persistentStorage: storage)
        settingsModule = RestSettingsModule(restService: restService, withAccountModule: accountModule)
        loginModule = RestLoginModule(restService: restService, withSettingsModule: settingsModule, withAccountModule: accountModule)
        vocabularyModule = RestVocabularyModule(restService: restService, withTranslationService: restService, withAccountModule: accountModule, withVocabularyWordsRepository: wordRepository, withJavaUtilConcurrentExecutor: JavaUtilConcurrentExecutors.newCachedThreadPool())
        //vocabularyModule = StubVocabularyModule(accountModule: accountModule, withVocabularyWordsRepository: wordRepository)
        flashcardsModule = RestFlashcardsModule(restService: restService)
        //flashcardsModule = StubFlashcardsModule()
        
        changeRootViewController(buildLoginViewController(), animated: false)
        
        let paths = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)
        print(paths[0])
        
        window!.makeKeyAndVisible()
        return true
    }
    
    func buildLoginViewController() -> UIViewController {
        let presenter = LoginPresenter(accountModule: accountModule, withLoginModule: loginModule, withJavaUtilConcurrentExecutor: mainThreadExecutor)
        return LoginViewController(presenter: presenter)
    }
    
    func buildMainViewController() -> UIViewController {
        let mainPresenter = MainPresenter(accountModule: accountModule, withJavaUtilConcurrentExecutor: mainThreadExecutor)
        let listPrenseter = VocabularyListPresenter(vocabularyModule: vocabularyModule, withVocabularyNavigator: mainPresenter, withJavaUtilConcurrentExecutor: mainThreadExecutor)
        let wordPresenter = VocabularyWordPresenter(javaUtilConcurrentExecutor: mainThreadExecutor)
        let mainVC = MainViewController(mainPresenter, listPrensenter: listPrenseter, wordPresenter: wordPresenter)
        
        let drawerPresenter = DrawerPresenter(mainPresenter: mainPresenter, withAccountModule: accountModule, withSettingsModule: settingsModule, withJavaUtilConcurrentExecutor: mainThreadExecutor)
        let drawerVC = DrawerViewController(presenter: drawerPresenter)
        
        let drawer = DrawerController(centerViewController: mainVC, leftDrawerViewController: drawerVC)
        drawer.openDrawerGestureModeMask = .BezelPanningCenterView
        drawer.closeDrawerGestureModeMask = .PanningCenterView
        // TODO: add custom .CloseDrawer
        drawer.centerHiddenInteractionMode = .NavigationBarOnly
        drawer.showsShadows = false
        drawer.shouldStretchDrawer = false
        
        return drawer;
    }
    
    func buildCardsViewController() -> UIViewController {
        let presenter = FlashcardsPresenter(flashcardsModule: flashcardsModule, withJavaUtilConcurrentExecutor: mainThreadExecutor)
        let cardsController:CardsViewController = CardsViewController(presenter)
        let navCardsController:UINavigationController = UINavigationController(rootViewController: cardsController)
        navCardsController.navigationBar.barTintColor = UIColor.flashcardsPrimary()
        navCardsController.navigationBar.tintColor = UIColor.whiteColor()
        
        return navCardsController
    }
    
    func buildSettingsViewController() -> UIViewController {
        let settingsController:SettingsViewController = SettingsViewController()
        let navController:UINavigationController = UINavigationController(rootViewController: settingsController)
        navController.navigationBar.barTintColor = UIColor.flashcardsPrimary()
        navController.navigationBar.tintColor = UIColor.whiteColor()
        return navController
    }
    
    func navigateToLogin() {
        accountModule?.setUserDataWithUserData(nil)
        accountModule?.setUserIdWithNSString(nil)
        let loginVC = buildLoginViewController()
        changeRootViewController(loginVC, animated: true)
    }
    
    func changeRootViewController(controller:UIViewController!, animated:Bool) {
        if (self.window!.rootViewController == nil || !animated) {
            self.window!.rootViewController = controller
            return
        }
        
        let snapshot:UIView = self.window!.snapshotViewAfterScreenUpdates(true)!
        controller.view.addSubview(snapshot)
        
        self.window!.rootViewController = controller;
        
        UIView.animateWithDuration(0.3, animations: {() in
            snapshot.layer.opacity = 0;
            snapshot.layer.transform = CATransform3DMakeScale(1.5, 1.5, 1.5);
            }, completion: {
                (value: Bool) in
                snapshot.removeFromSuperview();
        });
    }
    
    class func sharedAppDelegate() -> AppDelegate {
        return UIApplication.sharedApplication().delegate as! AppDelegate;
    }
    
    func applicationWillResignActive(application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }
    
    func applicationDidEnterBackground(application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }
    
    func applicationWillEnterForeground(application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }
    
    func applicationDidBecomeActive(application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }
    
    func applicationWillTerminate(application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
        // Saves changes in the application's managed object context before the application terminates.
        self.saveContext()
    }
    
    // MARK: - Core Data stack
    
    lazy var applicationDocumentsDirectory: NSURL = {
        // The directory the application uses to store the Core Data store file. This code uses a directory named "com.triangleleft.Flashcards" in the application's documents Application Support directory.
        let urls = NSFileManager.defaultManager().URLsForDirectory(.DocumentDirectory, inDomains: .UserDomainMask)
        return urls[urls.count-1]
    }()
    
    lazy var managedObjectModel: NSManagedObjectModel = {
        // The managed object model for the application. This property is not optional. It is a fatal error for the application not to be able to find and load its model.
        let modelURL = NSBundle.mainBundle().URLForResource("Flashcards", withExtension: "momd")!
        return NSManagedObjectModel(contentsOfURL: modelURL)!
    }()
    
    lazy var persistentStoreCoordinator: NSPersistentStoreCoordinator = {
        // The persistent store coordinator for the application. This implementation creates and returns a coordinator, having added the store for the application to it. This property is optional since there are legitimate error conditions that could cause the creation of the store to fail.
        // Create the coordinator and store
        let coordinator = NSPersistentStoreCoordinator(managedObjectModel: self.managedObjectModel)
        let url = self.applicationDocumentsDirectory.URLByAppendingPathComponent("Flashcards.db")
        var failureReason = "There was an error creating or loading the application's saved data."
        do {
            try coordinator.addPersistentStoreWithType(NSSQLiteStoreType, configuration: nil, URL: url, options: nil)
        } catch {
            // Report any error we got.
            var dict = [String: AnyObject]()
            dict[NSLocalizedDescriptionKey] = "Failed to initialize the application's saved data"
            dict[NSLocalizedFailureReasonErrorKey] = failureReason
            
            dict[NSUnderlyingErrorKey] = error as NSError
            let wrappedError = NSError(domain: "com.triangleleft.flashcards", code: 9999, userInfo: dict)
            // Replace this with code to handle the error appropriately.
            // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            NSLog("Unresolved error \(wrappedError), \(wrappedError.userInfo)")
            #if DEBUG
                abort()
            #endif
        }
        
        return coordinator
    }()
    
    lazy var managedObjectContext: NSManagedObjectContext = {
        // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.) This property is optional since there are legitimate error conditions that could cause the creation of the context to fail.
        let coordinator = self.persistentStoreCoordinator
        var managedObjectContext = NSManagedObjectContext(concurrencyType: .MainQueueConcurrencyType)
        managedObjectContext.persistentStoreCoordinator = coordinator
        return managedObjectContext
    }()
    
    // MARK: - Core Data Saving support
    
    func saveContext () {
        if managedObjectContext.hasChanges {
            do {
                try managedObjectContext.save()
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                let nserror = error as NSError
                NSLog("Couldn't save data: \(nserror), \(nserror.userInfo)")
                #if DEBUG
                    abort()
                #endif
            }
        }
    }
    
}

extension UITextField {
    class func connectFields(fields:[UITextField]) -> Void {
        guard let last = fields.last else {
            return
        }
        for i in 0 ..< fields.count - 1 {
            fields[i].returnKeyType = .Next
            fields[i].addTarget(fields[i+1], action: #selector(UIResponder.becomeFirstResponder), forControlEvents: .EditingDidEndOnExit)
        }
        last.returnKeyType = .Done
        last.addTarget(last, action: #selector(UIResponder.resignFirstResponder), forControlEvents: .EditingDidEndOnExit)
    }
}

