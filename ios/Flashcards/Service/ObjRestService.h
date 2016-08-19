//
//  ObjRestService.h
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 19.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <FlashcardsCore.h>

@interface ObjRestService : NSObject <RestService>

-(instancetype)initWithGson:(ComGoogleGsonGson *)gson;

@end
