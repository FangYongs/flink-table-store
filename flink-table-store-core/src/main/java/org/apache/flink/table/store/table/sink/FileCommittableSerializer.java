/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.store.table.sink;

import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.table.store.file.io.CompactIncrement;
import org.apache.flink.table.store.file.io.DataFileMetaSerializer;
import org.apache.flink.table.store.file.io.NewFilesIncrement;
import org.apache.flink.table.store.io.DataInputDeserializer;
import org.apache.flink.table.store.io.DataInputView;
import org.apache.flink.table.store.io.DataOutputView;
import org.apache.flink.table.store.io.DataOutputViewStreamWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.flink.table.store.file.utils.SerializationUtils.deserializeBinaryRow;
import static org.apache.flink.table.store.file.utils.SerializationUtils.serializeBinaryRow;

/** {@link SimpleVersionedSerializer} for {@link FileCommittable}. */
public class FileCommittableSerializer implements SimpleVersionedSerializer<FileCommittable> {

    private static final int CURRENT_VERSION = 2;

    private final DataFileMetaSerializer dataFileSerializer;

    public FileCommittableSerializer() {
        this.dataFileSerializer = new DataFileMetaSerializer();
    }

    @Override
    public int getVersion() {
        return CURRENT_VERSION;
    }

    @Override
    public byte[] serialize(FileCommittable obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputViewStreamWrapper view = new DataOutputViewStreamWrapper(out);
        serialize(obj, view);
        return out.toByteArray();
    }

    public void serializeList(List<FileCommittable> list, DataOutputView view) throws IOException {
        view.writeInt(list.size());
        for (FileCommittable fileCommittable : list) {
            serialize(fileCommittable, view);
        }
    }

    private void serialize(FileCommittable obj, DataOutputView view) throws IOException {
        serializeBinaryRow(obj.partition(), view);
        view.writeInt(obj.bucket());
        dataFileSerializer.serializeList(obj.newFilesIncrement().newFiles(), view);
        dataFileSerializer.serializeList(obj.newFilesIncrement().changelogFiles(), view);
        dataFileSerializer.serializeList(obj.compactIncrement().compactBefore(), view);
        dataFileSerializer.serializeList(obj.compactIncrement().compactAfter(), view);
        dataFileSerializer.serializeList(obj.compactIncrement().changelogFiles(), view);
    }

    @Override
    public FileCommittable deserialize(int version, byte[] serialized) throws IOException {
        checkVersion(version);
        DataInputDeserializer view = new DataInputDeserializer(serialized);
        return deserialize(view);
    }

    public List<FileCommittable> deserializeList(int version, DataInputView view)
            throws IOException {
        checkVersion(version);
        int length = view.readInt();
        List<FileCommittable> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(deserialize(view));
        }
        return list;
    }

    private void checkVersion(int version) {
        if (version != CURRENT_VERSION) {
            throw new UnsupportedOperationException(
                    "Expecting FileCommittable version to be "
                            + CURRENT_VERSION
                            + ", but found "
                            + version
                            + ".\nFileCommittable is not a compatible data structure. "
                            + "Please restart the job afresh (do not recover from savepoint).");
        }
    }

    private FileCommittable deserialize(DataInputView view) throws IOException {
        return new FileCommittable(
                deserializeBinaryRow(view),
                view.readInt(),
                new NewFilesIncrement(
                        dataFileSerializer.deserializeList(view),
                        dataFileSerializer.deserializeList(view)),
                new CompactIncrement(
                        dataFileSerializer.deserializeList(view),
                        dataFileSerializer.deserializeList(view),
                        dataFileSerializer.deserializeList(view)));
    }
}
