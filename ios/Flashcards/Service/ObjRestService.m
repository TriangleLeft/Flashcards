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
#import "IOSClass.h"

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
    RxSubscriber* subsriber = (RxSubscriber*)t;
    _handler(subsriber);
}

@end

@interface ObjRestService()

- (NSURLRequest *)requestWithMethod:(NSString *)method url:(NSURL *)url body:(id)body;

@property (strong, nonatomic) ComGoogleGsonGson* gson;

@end

@implementation ObjRestService

-(instancetype)initWithGson:(ComGoogleGsonGson *)gson {
    _gson = gson;

    return self;
}

- (RxObservable *)loginWithLoginRequestController:(LoginRequestController *)model {
    NSURLRequest *req = [self requestWithMethod:@"POST" url:[self urlWithPath:[RestService PATH_LOGIN]] body:model];
    return [self observableWithRequest:req responseModelClass:LoginResponseModel_class_()];
}

- (RxObservable *)getVocabularyListWithLong:(jlong)timestamp {
    NSString* timestampString = [NSString stringWithFormat:@"%lld", timestamp];
    NSArray<NSURLQueryItem *> *params = @[[[NSURLQueryItem alloc] initWithName:[RestService QUERY_TIMESTAMP] value:timestampString]];
    NSURLRequest *req = [self requestWithMethod:@"GET" url:[self urlWithPath:[RestService PATH_VOCABULARY] queryParams:params]];
    return [self observableWithRequest:req responseModelClass:VocabularyResponseModel_class_()];

}

- (RxObservable *)getFlashcardDataWithInt:(jint)count
                              withBoolean:(jboolean)allowPartialDeck
                                 withLong:(jlong)timestamp {
    return NULL;
}

- (RxObservable *)postFlashcardResultsWithFlashcardResultsController:(FlashcardResultsController *)model {
    //NSURLRequest *req = [self requestWithMethod:@"POST" url:[self urlWithPath:] body:model];
    return NULL;
}

- (RxObservable *)switchLanguageWithSwitchLanguageController:(SwitchLanguageController *)controller {
    return NULL;
}

- (RxObservable *)getUserDataWithNSString:(NSString *)userId {
    NSArray<NSURLQueryItem *> *params = @[[[NSURLQueryItem alloc] initWithName:[RestService QUERY_USERID] value:userId]];
    NSURLRequest *req = [self requestWithMethod:@"GET" url:[self urlWithPath:[RestService PATH_USERDATA] queryParams:params]];
    return [self observableWithRequest:req responseModelClass:UserDataModel_class_()];
}

- (RxObservable *)getTranslationWithNSString:(NSString *)languageIdFrom
                                withNSString:(NSString *)languageIdTo
                                withNSString:(NSString *)tokens {
    NSString* urlString = [[NSString alloc] initWithFormat:@"%@/%@/%@", [RestService URL_TRANSLATION], languageIdFrom, languageIdTo];
    NSURLComponents *components = [[NSURLComponents alloc] initWithString:urlString];
    components.queryItems = @[[[NSURLQueryItem alloc] initWithName:[RestService QUERY_TOKENS] value:tokens]];
    NSURLRequest *req = [self requestWithMethod:@"GET" url:components.URL];
    return [self observableWithRequest:req responseModelClass:WordTranslationModel_class_()];
}

- (RxObservable *)observableWithRequest:(NSURLRequest *)request responseModelClass:(IOSClass *)clazz {
    RxObservable* observable = [RxObservable createWithRxObservable_OnSubscribe:[[MyClosure alloc] initWithClosure:^(RxSubscriber *subscriber) {
        AFHTTPSessionManager *manager = [AFHTTPSessionManager new];
        manager.responseSerializer = [[GsonResponseSerializer alloc] initWithClass:clazz gson:_gson];
        
        [[manager dataTaskWithRequest:request completionHandler:^(NSURLResponse * _Nonnull response, id  _Nullable responseObject, NSError * _Nullable error) {
            
            if (!error) {
                [subscriber onNextWithId:responseObject];
            } else {
                NSLog(@"Error: %@, %@, %@", error, response, responseObject);
            }
            [subscriber onCompleted];
        }] resume];
        
    }]];
    return observable;
}

- (NSURL *)urlWithPath:(NSString *)path {
    return [self urlWithPath:path queryParams:nil];
}

- (NSURL *)urlWithPath:(NSString *)path queryParams:(NSArray<NSURLQueryItem *> *)queryParams {
    NSURLComponents *components = [[NSURLComponents alloc] init];
    components.scheme = [RestService BASE_SCHEME];
    components.host = [RestService BASE_URL];
    components.path = path;
    if (queryParams) {
        components.queryItems = queryParams;
    }
    return components.URL;
}

- (NSURLRequest *)requestWithMethod:(NSString *)method url:(NSURL*)url {
    return [self requestWithMethod:method url:url body:nil];
}

- (NSURLRequest *)requestWithMethod:(NSString *)method url:(NSURL*)url body:(id)body {
    NSMutableURLRequest *req = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData  timeoutInterval:60];
    [req setHTTPMethod:method];
    [req setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    if (body) {
        [req setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [req setHTTPBody:[[[self gson] toJsonWithId:body] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    return req;
}

@end
