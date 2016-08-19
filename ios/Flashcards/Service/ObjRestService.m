//
//  ObjRestService.m
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 19.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

#import "ObjRestService.h"
#import <AFNetworking/AFNetworking.h>
#import "GsonResponseSerializer.h"
#import <AFNetworkActivityLogger/AFNetworkActivityLogger.h>
#import <AFNetworkActivityLogger/AFNetworkActivityConsoleLogger.h>

typedef void(^RxSubscriberHandler)(RxSubscriber *subscriber);

@interface MyClosure : NSObject <RxObservable_OnSubscribe>
@property (nonatomic, copy) RxSubscriberHandler handler;
@end
@implementation MyClosure


-(instancetype)initWithClosure:(RxSubscriberHandler)closure {
    _handler = closure;
    return self;
}

- (void)callWithId:(id)t {
    NSLog(@"Called with %@", t);
    RxSubscriber* subsriber = (RxSubscriber*)t;
    _handler(subsriber);
    }

@end


@implementation ObjRestService {
    ComGoogleGsonGson *_gson;
}

-(instancetype)initWithGson:(ComGoogleGsonGson *)gson {
    _gson = gson;
    AFNetworkActivityConsoleLogger *consoleLogger = [AFNetworkActivityConsoleLogger new];
    [consoleLogger setLevel:AFLoggerLevelDebug];
    [[AFNetworkActivityLogger sharedLogger] removeLogger:[[[AFNetworkActivityLogger sharedLogger] loggers] anyObject]];
    [[AFNetworkActivityLogger sharedLogger] addLogger:consoleLogger];
    [[AFNetworkActivityLogger sharedLogger] startLogging];
    
    return self;
}

- (RxObservable *)loginWithComTriangleleftFlashcardsServiceLoginRestLoginRequestController:(ComTriangleleftFlashcardsServiceLoginRestLoginRequestController *)model {
    RxObservable* observable = [RxObservable createWithRxObservable_OnSubscribe:[[MyClosure alloc] initWithClosure:^(RxSubscriber *subscriber) {
        NSURL *baseUrl = [[NSURL alloc] initWithString:[ComTriangleleftFlashcardsServiceRestService BASE_URL]];
        AFHTTPSessionManager *manager = [[AFHTTPSessionManager alloc] initWithBaseURL:baseUrl];
        manager.responseSerializer = [[GsonResponseSerializer alloc] initWithModel:ComTriangleleftFlashcardsServiceLoginRestLoginResponseModel_class_() gson:_gson];
        
        NSString * jsonString = [_gson toJsonWithId:model];
    
        NSURLComponents *components = [[NSURLComponents alloc] init];
        components.scheme = [ComTriangleleftFlashcardsServiceRestService BASE_SCHEME];
        components.host = [ComTriangleleftFlashcardsServiceRestService BASE_URL];
        components.path = [ComTriangleleftFlashcardsServiceRestService POST_LOGIN];
        
        NSMutableURLRequest *req = [NSMutableURLRequest requestWithURL:components.URL cachePolicy:NSURLRequestReloadIgnoringCacheData  timeoutInterval:60];
        [req setHTTPMethod:@"POST"];
        [req setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [req setValue:@"application/json" forHTTPHeaderField:@"Accept"];
        [req setHTTPBody:[jsonString dataUsingEncoding:NSUTF8StringEncoding]];
        
        
        [[manager dataTaskWithRequest:req completionHandler:^(NSURLResponse * _Nonnull response, id  _Nullable responseObject, NSError * _Nullable error) {
            
            if (!error) {
                NSLog(@"Reply JSON: %@", responseObject);
                
                [subscriber onNextWithId:responseObject];
            } else {
                NSLog(@"Error: %@, %@, %@", error, response, responseObject);
            }
        }] resume];

    }]];
    return observable;
}

- (RxObservable *)getVocabularyListWithLong:(jlong)timestamp {
    return NULL;
}

- (RxObservable *)getFlashcardDataWithInt:(jint)count
                              withBoolean:(jboolean)allowPartialDeck
                                 withLong:(jlong)timestamp {
    return NULL;
}

- (RxObservable *)postFlashcardResultsWithComTriangleleftFlashcardsServiceCardsRestFlashcardResultsController:(ComTriangleleftFlashcardsServiceCardsRestFlashcardResultsController *)model {
    return NULL;
}

- (RxObservable *)switchLanguageWithNSString:(NSString *)languageId {
    return NULL;
}

- (RxObservable *)getUserDataWithNSString:(NSString *)userId {
    return NULL;
}

- (RxObservable *)getTranslationWithNSString:(NSString *)languageIdFrom
                                withNSString:(NSString *)languageIdTo
                                withNSString:(NSString *)tokens {
    return NULL;
}

@end
