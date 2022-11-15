redis.call('set',KEYS[1], ARGV[1])
redis.call('decr',KEYS[2])