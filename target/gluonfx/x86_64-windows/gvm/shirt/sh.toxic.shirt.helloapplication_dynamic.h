#ifndef __SH_TOXIC_SHIRT_HELLOAPPLICATION_H
#define __SH_TOXIC_SHIRT_HELLOAPPLICATION_H

#include <graal_isolate_dynamic.h>


#if defined(__cplusplus)
extern "C" {
#endif

typedef int (*run_main_fn_t)(int argc, char** argv);

typedef void (*vmLocatorSymbol_fn_t)(graal_isolatethread_t* thread);

#if defined(__cplusplus)
}
#endif
#endif
