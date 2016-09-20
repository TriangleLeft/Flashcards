//
//  GsonResponseSerializer.m
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 19.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

#import <FlashcardsCore.h>
#import "GsonResponseSerializer.h"

@implementation GsonResponseSerializer {
    ComGoogleGsonGson *_gson;
    IOSClass *_class;
}

-(instancetype)initWithClass:(IOSClass *)class gson:(ComGoogleGsonGson *)gson {
    _class = class;
    _gson = gson;
    return self;
}

- (id)responseObjectForResponse:(NSURLResponse *)response
                           data:(NSData *)data
                          error:(NSError *__autoreleasing *)error
{
    id responseObject = nil;

    if ([self validateResponse:(NSHTTPURLResponse *)response data:data error:error]) {
        NSString * json = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        //NSLog(@"Reponse: %@", json);
        responseObject = [_gson fromJsonWithNSString:json withIOSClass:_class];
    } else {
        NSLog(@"Validation failed");
    }
    
    
    return responseObject;
}


@end
