package com.example.cloud_resource_tracker.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GroupGrantee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3 s3Client;

    public List<String> findPublicBuckets() {
        return s3Client.listBuckets().stream()
                .filter(bucket -> isBucketPublic(bucket.getName()))
                .map(Bucket::getName)
                .collect(Collectors.toList());
    }

    private boolean isBucketPublic(String bucketName) {
        AccessControlList acl = s3Client.getBucketAcl(bucketName);
        return acl.getGrantsAsList().stream()
                .anyMatch(grant -> grant.getGrantee().equals(GroupGrantee.AllUsers));
    }
}
