// Polyfills for the application
// Some third-party libraries (eg. stompjs, crypto) expect a Node-like
// `global` variable. Angular no longer provides it automatically, so we
// define it here rather than patching every import.

(window as any).global = window as any;

// Additional polyfills can be added below this line if needed
